package com.example.runner;

import com.example.domain.NodeInstance;
import com.example.domain.NodeStatusEnum;
import com.example.domain.NodeTypeEnum;
import com.example.service.NodeService;
import com.example.util.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
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
        if (nodeInstance.getNodeType().equals(Const.SWITCH)) {
            switchNodeRunner.initSwitchNode(nodeInstance);
        } else if (nodeInstance.getNodeType().equals(Const.FLOW)) {
            flowRunner.initFlow(nodeInstance);
        } else if (nodeInstance.getNodeType().equals(Const.TASK)) {
            taskNodeRunner.initNode(nodeInstance);
        } else {
            logger.error("unknown node type: |Default|");
        }
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 20000)
    public void initNode() throws Exception {
        logger.info("node runner manager init node");
        List<NodeInstance> initFlowNodeInst = findInitNode();
        for (NodeInstance flowNode : initFlowNodeInst) {
            init(flowNode);
        }
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 20000)
    public void runningFlowNode() throws Exception {
        List<NodeInstance> runningFlowNode = findRunningFlowNode();
        for (NodeInstance nodeInstance : runningFlowNode) {
            flowRunner.runningFlow(nodeInstance);
        }
    }

    public List<NodeInstance> findInitNode() {
        NodeInstance initNode = NodeInstance.builder()
                .nodeStatus(Const.INIT)
                .build();
        List<NodeInstance> initNodeInstList = nodeService.findNode(initNode);
        return initNodeInstList;
    }

    public List<NodeInstance> findRunningFlowNode() {
        NodeInstance runningFlowNode = NodeInstance.builder()
                .nodeStatus(Const.RUNNING)
                .nodeType(Const.FLOW)
                .build();
        List<NodeInstance> runningFlowNodeInstList = nodeService.findNode(runningFlowNode);
        return runningFlowNodeInstList;
    }

}
