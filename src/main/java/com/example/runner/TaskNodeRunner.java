package com.example.runner;

import com.example.annotation.Input;
import com.example.api.TaskBuilder;
import com.example.api.TaskResult;
import com.example.domain.ArgInfo;
import com.example.domain.NodeInstance;
import com.example.service.NodeService;
import com.example.util.JsonHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class TaskNodeRunner {
    @Autowired
    NodeService nodeService;

    public void initNode(NodeInstance nodeInstance) throws JsonProcessingException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // node wait executed
        String className = nodeInstance.getClassName();
        // const args，假设全是简单类型
        String constArgs = nodeInstance.getArg();
        Map<String, ArgInfo> argInfoMap = JsonHelper.getMapper()
                .readValue(constArgs, Map.class);
        Class<TaskBuilder> clazz = (Class<TaskBuilder>) Class.forName(className);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Input.class)) {
                continue;
            }
            String fieldName = field.getName();
            ArgInfo argInfo = argInfoMap.get(fieldName);
            field.setAccessible(true);
            // 这里全部整合成 string 类型，可能会有问题
            field.set(instance, argInfo.getValue());
        }

        TaskBuilder taskInst = (TaskBuilder) instance;
        TaskResult result = taskInst.execute();
        nodeInstance.setNodeStatus(result.getStatus());
        nodeService.update(nodeInstance);
    }

}
