package com.example.runner;

import com.example.annotation.Input;
import com.example.annotation.Output;
import com.example.api.TaskBuilder;
import com.example.api.TaskResult;
import com.example.domain.ArgInfo;
import com.example.domain.NodeInstance;
import com.example.service.NodeService;
import com.example.util.Const;
import com.example.util.JsonHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskNodeRunner {
    private Logger logger = LoggerFactory.getLogger(TaskNodeRunner.class);

    @Autowired
    NodeService nodeService;

    public void initNode(NodeInstance nodeInstance) throws JsonProcessingException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // node wait executed
        String className = nodeInstance.getClassName();
        // const args，假设全是简单类型
        String constArgs = nodeInstance.getArg();

        Map<String, ArgInfo> argInfoMap = new HashMap<>();
        if (!StringUtils.isEmpty(constArgs)) {
            argInfoMap = JsonHelper.getMapper().readValue(constArgs, new TypeReference<Map<String, ArgInfo>>(){});
        }

        Class<TaskBuilder> clazz = (Class<TaskBuilder>) Class.forName(className);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();

        // 静态参数填充
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Input.class)) {
                continue;
            }
            String fieldName = field.getName();
            ArgInfo argInfo = argInfoMap.get(fieldName);
            field.setAccessible(true);
            if (argInfo == null) {
                logger.error("failed to get args of name: |{}|", fieldName);
                continue;
            }

            if (!argInfo.getType().equals(Const.ConstArg)) {
                continue;
            }

            // 这里全部整合成 string 类型，可能会有问题
            field.set(instance, argInfo.getValue());
        }

        // 动态参数填充
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Input.class)) {
                continue;
            }
            String fieldName = field.getName();
            ArgInfo argInfo = argInfoMap.get(fieldName);
            if (argInfo == null) {
                logger.error("failed to get dynamic args of name: |{}|", fieldName);
                continue;
            }
            if (!argInfo.getType().equals(Const.DynamicArg)) {
                continue;
            }
            String from = (String) argInfo.getValue();
            String srcNode = from.split("\\.")[0];
            String srcField = from.split("\\.")[1];
            NodeInstance querySrcParam = NodeInstance.builder()
                    .parentFlowId(nodeInstance.getParentFlowId())
                    .name(srcNode)
                    .build();
            List<NodeInstance> instanceList = nodeService.findNode(querySrcParam);
            if (instanceList.size() != 1) {
                logger.error("target instance list not one");
                continue;
            }
            NodeInstance srcNodeInst = instanceList.get(0);
            String output = srcNodeInst.getOutput();
            Map<String, Object> outputMap = JsonHelper.getMapper().readValue(output, new TypeReference<Map<String, Object>>() {});
            if (!outputMap.containsKey(srcField)) {
                logger.error("src node has no field named: |{}|", srcField);
                continue;
            }
            Object fieldObj = outputMap.get(srcField);
            Object target = JsonHelper.getMapper().readValue(JsonHelper.getMapper().writeValueAsString(fieldObj),
                    field.getType());
            field.set(instance, target);
        }

        // 填充 output
        String output = nodeInstance.getOutput();
        if (!StringUtils.isEmpty(output)) {
            Map<String, Object> outputMap = JsonHelper.getMapper().readValue(output, new TypeReference<Map<String, Object>>() {});
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Output.class)) {
                    continue;
                }
                String fieldName = field.getName();
                if (!outputMap.containsKey(fieldName)) {
                    continue;
                }

                Class clz = field.getType();
                // 能直接设置成 src 吗?
                Object src = outputMap.get(fieldName);
                Object target = JsonHelper.getMapper().readValue(JsonHelper.getMapper().writeValueAsString(src), clz);
                field.setAccessible(true);
                field.set(instance, target);
            }
        }


        TaskBuilder taskInst = (TaskBuilder) instance;
        TaskResult result = taskInst.execute();
        // 持久化 outputs，重新设置一遍
        Map<String, Object> outputMap = new HashMap<>();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Output.class)) {
                continue;
            }
            String fieldName = field.getName();
            field.setAccessible(true);
            outputMap.put(fieldName, field.get(instance));
        }
        nodeInstance.setOutput(JsonHelper.getMapper().writeValueAsString(outputMap));
        nodeInstance.setNodeStatus(result.getStatus());
        nodeService.update(nodeInstance);
    }

    // TODO
    private TaskBuilder assembleTaskInst(NodeInstance nodeInstance) {
        // assemble input
        // assemble output
        String output = nodeInstance.getOutput();
        return null;
    }

}
