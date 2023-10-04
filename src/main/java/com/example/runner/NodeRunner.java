package com.example.runner;

import com.example.domain.NodeDefinition;
import com.example.domain.NodeInstance;
import com.example.domain.SwitchDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class NodeRunner {
    private Logger logger = LoggerFactory.getLogger(NodeRunner.class);

    public void init(NodeInstance nodeInstance) {
        switch (nodeInstance.getNodeType()) {
            case "SwitchNode":
            case "ParallelNode":
            case "TaskNode":
            case "FlowNode":
            case "Default":

        }

        if (nodeInstance.getNodeType().equals("SwitchNode")) {
            runSwitchNode(nodeInstance);
        }
    }

    public void runSwitchNode(NodeInstance switchNode) {
        String arg = switchNode.getArg();
        // convert arg to HashMap
        Map<String, Long> conditionToNode = new HashMap<>();
        // evaluateCondition
        // set node status to init
    }

}
