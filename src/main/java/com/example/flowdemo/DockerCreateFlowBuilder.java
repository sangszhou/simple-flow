//package com.example.flowdemo;
//
//import com.example.api.FlowBuilder;
//import com.example.domain.FlowDefinition;
//import com.example.domain.FlowInput;
//import com.example.domain.NodeDefinition;
//import com.example.domain.TaskDefinition;
//
//public class DockerCreateFlowBuilder implements FlowBuilder {
//
//    @Override
//    public NodeDefinition build(FlowInput flowInput) {
//        FlowInput subFlowInput = FlowInput.builder()
//                .operator(flowInput.getOperator())
//                .build();
//
//        FlowDefinition createDockerDef = FlowDefinition.builder()
//                .flowName("createDockerFlow")
//                .flowInput(subFlowInput)
//                .build();
//
//        TaskDefinition syncCmdb = TaskDefinition.builder()
//                .taskDefId("syncTaskDefId")
//                .taskName("syncCmdb")
//                .build();
//
//        createDockerDef.followBy(syncCmdb);
//        return createDockerDef;
//    }
//}
