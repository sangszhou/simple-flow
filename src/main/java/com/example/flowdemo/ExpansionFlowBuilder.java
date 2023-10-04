//package com.example.flowdemo;
//
//import com.example.api.FlowBuilder;
//import com.example.domain.FlowInput;
//import com.example.domain.NodeDefinition;
//import com.example.domain.TaskDefinition;
//
//// 遇到的问题，对于扩容，能扩出来几台就扩几台，扩容完毕后，要立即更新库存。
//// 但是即使没扩出来，但是命令下发了，可能还是会更新库存。
//// 所以对于扩容，可能也得面向终态，不是处理 +/- 的问题，而是实时更新数据
//// 这意味着，sync cmdb 也不能用了，而是要面向终态的 sync
//public class ExpansionFlowBuilder implements FlowBuilder {
//
//    @Override
//    public NodeDefinition build(FlowInput flowInput) {
//        TaskDefinition approvalTask = TaskDefinition
//                .builder()
//                .taskName("扩容审批")
//                .taskDefId("expansionApproval")
//                .className("UserTask")
//                .build();
//
//        TaskDefinition expansionTask = TaskDefinition.builder()
//                .taskName("部署组扩容")
//                .taskDefId("app_expansion")
//                .className("AppExpansionTask")
//                .build();
//
//        TaskDefinition syncCmdb = TaskDefinition.builder()
//                .taskName("同步 CMDB")
//                .taskDefId("sync_cmdb")
//                .className("SyncCmdbTask")
//                .build();
//
//        TaskDefinition updateQuota = TaskDefinition.builder()
//                .taskName("同步 Quota")
//                .taskDefId("syncOneCloud")
//                .className("syncOneCloud")
//                .build();
//
//        approvalTask
//                .followBy(expansionTask)
//                .followBy(syncCmdb)
//                .followBy(updateQuota);
//
//        return approvalTask;
//    }
//}
