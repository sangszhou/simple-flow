package com.example.demo.app_lifecycle.flow;

import com.example.api.FlowBuilder;
import com.example.api.TaskBuilder;
import com.example.demo.app_lifecycle.task.BindEcsToLbTask;
import com.example.demo.app_lifecycle.task.CreateEcsTask;
import com.example.domain.FlowInput;
import com.example.domain.NodeDefinition;
import com.example.domain.TaskDefinition;
import com.fasterxml.jackson.core.JsonProcessingException;

public class EcsCreateFlow implements FlowBuilder {

    @Override
    public NodeDefinition build(FlowInput flowInput) throws JsonProcessingException {
        int ecsNum = Integer.parseInt(flowInput.getInput().get("num"));
        String lb = flowInput.getInput().get("lb");

        TaskDefinition createEcs = TaskDefinition.builder()
                .clazz(CreateEcsTask.class)
                .name("createEcsTask")
                .build()
                .constArg("num", ecsNum);

        TaskDefinition ecsBindLb = TaskDefinition.builder()
                .clazz(BindEcsToLbTask.class)
                .build()
                .constArg("lb", lb)
                .dynamicArg("ecsIpList", "createEcsTask.ecsIpList");

        createEcs.followBy(ecsBindLb);

        return createEcs;
    }
}
