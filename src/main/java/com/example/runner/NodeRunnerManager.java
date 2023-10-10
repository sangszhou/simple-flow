package com.example.runner;

import com.example.domain.NodeInstance;
import com.example.service.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeRunnerManager {
    private Logger logger = LoggerFactory.getLogger(NodeRunnerManager.class);
    @Autowired
    NodeService nodeService;
    @Autowired
    FlowRunner flowRunner;
    @Autowired
    SwitchNodeRunner switchNodeRunner;
    @Autowired
    TaskNodeRunner taskNodeRunner;

    public void init(NodeInstance nodeInstance) throws Exception {
        switch (nodeInstance.getNodeType()) {
            case "SwitchNode":
                switchNodeRunner.initSwitchNode(nodeInstance);
                break;
            case "ParallelNode":
                logger.error("unknown node type: |Parallel|");
            case "TaskNode":
                taskNodeRunner.initNode(nodeInstance);
                break;
            case "FlowNode":
                flowRunner.initFlow(nodeInstance);
                break;
            case "Default":
                logger.error("unknown node type: |Default|");
        }
    }

    @Scheduled(initialDelay = 10000, fixedRate = 10000)
    public void initNode() throws Exception {
        List<NodeInstance> initFlowNodeInst = findInitNode();
        for (NodeInstance flowNode : initFlowNodeInst) {
            init(flowNode);
        }
    }

    public List<NodeInstance> findInitNode() {
        NodeInstance initNode = NodeInstance.builder()
                .nodeStatus(0)
                .build();
        List<NodeInstance> initNodeInstList = nodeService.findNode(initNode);
        return initNodeInstList;
    }

}
