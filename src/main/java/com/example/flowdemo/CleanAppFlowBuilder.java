//package com.example.flowdemo;
//
//import com.example.api.FlowBuilder;
//import com.example.domain.FlowInput;
//import com.example.domain.NodeDefinition;
//import com.example.domain.TaskDefinition;
//
//public class CleanAppFlowBuilder implements FlowBuilder {
//
//    @Override
//    public NodeDefinition build(FlowInput flowInput) {
////        String appName = flowInput.getInput();
//        // query env from appName
//        // query workload from appName
//
//
//        TaskDefinition deleteWorkload = TaskDefinition.builder()
//                .taskDefId("deleteWorkload")
//                .taskName("删除部署组")
//                .build();
//
//        TaskDefinition deleteEnv = TaskDefinition.builder()
//                .taskDefId("deleteEnv")
//                .taskName("删除环境")
//                .build();
//
//        TaskDefinition deleteApp = TaskDefinition.builder()
//                .taskDefId("deleteApp")
//                .taskName("删除应用")
//                .build();
//
//        deleteWorkload
//                .conditional()
//                .when("success", deleteEnv);
//
//        deleteEnv.conditional().when("success", deleteApp);
//        return deleteWorkload;
//    }
//}
