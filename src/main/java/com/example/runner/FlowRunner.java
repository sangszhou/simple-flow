package com.example.runner;

import com.example.api.FlowBuilder;
import com.example.domain.FlowInput;
import com.example.domain.NodeDefinition;
import com.example.domain.NodeInstance;
import com.example.service.NodeService;
import com.example.util.JsonHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.LinkedList;
import java.util.List;

public class FlowRunner {
    private Logger logger = LoggerFactory.getLogger(FlowRunner.class);

    @Autowired
    NodeService nodeService;
    @Autowired
    FlowService flowService;

    /**
     * flowRunner 参数本身就是 nodeInstance，不需要额外的 flowNode 了
     * @param flowNode
     * @throws JsonProcessingException
     */
    public void initFlow(NodeInstance flowNode) throws Exception {
        FlowInput flowInput = JsonHelper.getMapper().readValue(flowNode.getArg(),
                FlowInput.class);
        String className = flowNode.getClassName();

        NodeDefinition firstNodeDefinition = ((FlowBuilder) Class.forName(className).newInstance()).build(flowInput);
        NodeInstance nodeInstance = flowService.parseNode(flowNode.getId(), firstNodeDefinition);
    }


}
