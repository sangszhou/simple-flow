package com.example.demo.app_lifecycle.flow;

import com.example.api.FlowBuilder;
import com.example.demo.app_lifecycle.task.ExpandWorkloadTask;
import com.example.domain.FlowDefinition;
import com.example.domain.FlowInput;
import com.example.domain.NodeDefinition;
import com.example.domain.TaskDefinition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppExpansionFlow implements FlowBuilder {
    private Logger logger = LoggerFactory.getLogger(AppExpansionFlow.class);

    @Override
    public NodeDefinition build(FlowInput flowInput) throws JsonProcessingException {
        String appName = (String) flowInput.getInput().get("appName");
        Integer num = (int) flowInput.getInput().get("num");
        String lb = (String)flowInput.getInput().get("lb");

        logger.info("app expand, appName:|{}|, num:|{}|, lb:|{}|",
                appName, num, lb);

        FlowInput subFlowInput = FlowInput.builder()
                .operator(flowInput.getOperator())
                .input(ImmutableMap.of(
                        "num", num,
                        "lb", lb))
                .build();

        // 独占池扩容
        FlowDefinition createEcsFlow = FlowDefinition.builder()
                .name("创建ECS")
                .clazz(EcsCreateFlow.class)
                .flowInput(subFlowInput)
                .build();

        // Pod 扩容
        TaskDefinition expandWorkload = TaskDefinition.builder()
                .clazz(ExpandWorkloadTask.class)
                .build()
                .constArg("num", num)
                .constArg("appName", appName)
                ;

        createEcsFlow
                .followBy(expandWorkload);

        return createEcsFlow;
    }
}
