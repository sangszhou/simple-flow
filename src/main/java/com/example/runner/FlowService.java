package com.example.runner;

import com.example.api.FlowBuilder;
import com.example.domain.*;
import com.example.service.NodeService;
import com.example.util.Const;
import com.example.util.JsonHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class FlowService {
    private Logger logger = LoggerFactory.getLogger(FlowService.class);

    @Autowired
    NodeService nodeService;

    public long startFlow(String flowName, FlowInput flowInput) {
//        return startFlow()
        return 0L;
    }

    public long startFlow(Class<? extends FlowBuilder> flowName, FlowInput flowInput) throws Exception {
        logger.info("start flow with flowName:|{}|, input: |{}|",
                flowName.getName(), flowInput.getInput());
        try {
            NodeDefinition firstNode = flowName.newInstance().build(flowInput);
            NodeInstance nodeInstance = NodeInstance.builder()
//                    .createTime(LocalDateTime.now())
//                    .updateTime(LocalDateTime.now())
//                    .del(false)
                    .nodeId(flowName.getName())
                    .name(flowName.getSimpleName())
                    .nodeType(Const.FLOW)
                    .nodeStatus(Const.INVALID)
//                    .arg(JsonHelper.getMapper().writeValueAsString(flowInput))
                    .build();

            nodeService.create(nodeInstance);
            parseNode(nodeInstance.getId(), firstNode);
            // 流程开始执行
            nodeInstance.setNodeStatus(Const.RUNNING);
            nodeService.update(nodeInstance);

            // find starter node to init
            return nodeInstance.getId();
        } catch (InstantiationException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw e;
        } catch (JsonProcessingException e) {
            throw e;
        }
    }

    // 不需要主动开始，可以靠控制节点的 running 逻辑来驱动
    public void startNode(NodeInstance flowNode) {
        List<NodeInstance> instanceList = nodeService.findNoPredecessorNode(flowNode);
        for (NodeInstance nodeInstance : instanceList) {
            nodeInstance.setNodeStatus(Const.INIT);
            nodeService.update(nodeInstance);
        }
    }

    public NodeInstance parseNode(long flowId, NodeDefinition nodeDefinition) throws Exception {
        NodeInstance nodeInstance = null;
        if (nodeDefinition instanceof TaskDefinition) {
            nodeInstance = parseTaskNode(flowId, (TaskDefinition) nodeDefinition);
        } else if (nodeDefinition instanceof SwitchDefinition) {
            nodeInstance = parseSwitchNode(flowId, (SwitchDefinition) nodeDefinition);
        } else if (nodeDefinition instanceof FlowDefinition) {
            nodeInstance = parseFlowNode(flowId, (FlowDefinition) nodeDefinition);
        }

        // 遍历后续节点
        List <NodeDefinition> nextNodeList = nodeDefinition.getNextNode();
        for (NodeDefinition nextNode : nextNodeList) {
            NodeInstance nextNodeInstance = parseNode(flowId, nextNode);
            nextNodeInstance.addPreNode(nodeInstance.getNodeId());
            nodeService.update(nextNodeInstance);
        }

        nodeService.create(nodeInstance);

        return nodeInstance;
    }

    private NodeInstance parseFlowNode(long flowId, FlowDefinition flowDefinition) throws JsonProcessingException {
        NodeInstance nodeInstance = NodeInstance.builder()
//                .createTime(LocalDateTime.now())
//                .updateTime(LocalDateTime.now())
                .parentFlowId(flowId)
//                .del(false)
                .nodeId(flowDefinition.getId())
                .name(flowDefinition.getName())
                .className(flowDefinition.getClazz().getName())
                .nodeStatus(Const.INVALID)
                .nodeType(Const.FLOW)
                .arg(JsonHelper.getMapper().writeValueAsString(flowDefinition.getFlowInput()))
                .build();
        return nodeInstance;
    }


    private NodeInstance parseTaskNode(long flowId, TaskDefinition taskDefinition) throws JsonProcessingException {
        String name = taskDefinition.getName();
        if (StringUtils.isEmpty(name)) {
            name = taskDefinition.getClassName();
        }

        NodeInstance nodeInstance = NodeInstance.builder()
//                .createTime(LocalDateTime.now())
//                .updateTime(LocalDateTime.now())
                .parentFlowId(flowId)
                .nodeStatus(Const.INVALID)
                .nodeType(Const.TASK)
//                .del(false)
                .nodeId(taskDefinition.getId())
                .name(name)
                .className(taskDefinition.getClassName())
                .arg(JsonHelper.getMapper().writeValueAsString(taskDefinition.getDetailArg()))
                .build();
        return nodeInstance;
    }

    private NodeInstance parseSwitchNode(long flowId, SwitchDefinition nodeDefinition) throws Exception {
        Map<String, NodeDefinition> conditionInfo = nodeDefinition.getConditionalInfo();
        Map<String, String> savedConditionInfo = new HashMap<>();
        for (String key : conditionInfo.keySet()) {
            savedConditionInfo.put(key, conditionInfo.get(key).getId());
        }

        NodeInstance nodeInstance = NodeInstance.builder()
//                .createTime(LocalDateTime.now())
//                .updateTime(LocalDateTime.now())
                .parentFlowId(flowId)
                .nodeId("switchNode:"+new Random().nextInt(1000))
                .name("switchNode")
//                .del(false)
                .nodeType(Const.SWITCH)
                .nodeStatus(Const.INVALID)
                .arg(JsonHelper.getMapper().writeValueAsString(savedConditionInfo))
                .build();

        // 处理 condition 节点
        for (NodeDefinition childNodeDefinition : conditionInfo.values()) {
            parseNode(flowId, childNodeDefinition);
        }

        return nodeInstance;
    }
}
