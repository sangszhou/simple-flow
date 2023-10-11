package com.example.runner;

import com.example.api.FlowBuilder;
import com.example.domain.FlowInput;
import com.example.domain.NodeDefinition;
import com.example.domain.NodeInstance;
import com.example.service.NodeService;
import com.example.util.Const;
import com.example.util.JsonHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlowRunner {
    private Logger logger = LoggerFactory.getLogger(FlowRunner.class);

    @Autowired
    FlowService flowService;
    @Autowired
    NodeService nodeService;

    /**
     * flowRunner 参数本身就是 nodeInstance，不需要额外的 flowNode 了
     * @param flowNode
     * @throws JsonProcessingException
     */
    public void initFlow(NodeInstance flowNode) throws Exception {
        logger.info("flow runner init nodeId:|{}|", flowNode.getName());

        FlowInput flowInput = JsonHelper.getMapper().readValue(flowNode.getArg(),
                FlowInput.class);
        String className = flowNode.getClassName();

        NodeDefinition firstNodeDefinition = ((FlowBuilder) Class.forName(className).newInstance())
                .build(flowInput);
        flowService.parseNode(flowNode.getId(), firstNodeDefinition);

        // find start task to run
        flowNode.setNodeStatus(Const.RUNNING);
        nodeService.update(flowNode);

        flowService.startNode(flowNode);
    }

    public void runningFlow(NodeInstance flowNode) {
        // 查看所有的孩子节点
        NodeInstance invalidNode = NodeInstance.builder()
                .parentFlowId(flowNode.getId())
                .build();

        List<NodeInstance> nodeInstanceList = nodeService.findNode(invalidNode);
        long nonSuccessCnt = nodeInstanceList.stream().map(it -> it.getNodeStatus())
                .filter(it -> !it.equals(Const.SUCCESS))
                .count();
        // 如果所有的孩子节点全部结束，那么父节点结束
        if (nonSuccessCnt == 0) {
            flowNode.setNodeStatus(Const.SUCCESS);
            nodeService.update(flowNode);
            return;
        }

        // 依次判断孩子节点是否可以前进 [可以利用缓存优化]
        for (NodeInstance nodeInstance : nodeInstanceList) {
            if (!nodeInstance.getNodeStatus().equals(Const.INVALID)) {
                continue;
            }

            String dep = nodeInstance.getPreNode();
            if (StringUtils.isEmpty(dep)) {
                // empty 的可以直接设置成 INIT
                nodeInstance.setNodeStatus(Const.INIT);
                nodeService.update(nodeInstance);
                continue;
            }

            // 非 INIT 状态的，可以先设置
            List<String> nodeName = Lists.newArrayList(dep.split(","));
            List<String> statusList = nodeName.stream().map(it -> {
                NodeInstance queryNode = NodeInstance.builder()
                        .parentFlowId(flowNode.getId())
                        .nodeId(it)
                        .build();
                return nodeService.findNode(queryNode).get(0).getNodeStatus();
            }).filter(it -> !it.equals(Const.SUCCESS))
                    .collect(Collectors.toList());
            // 依赖节点全部完成
            if (CollectionUtils.isEmpty(statusList)) {
                // current node ready to go
                nodeInstance.setNodeStatus(Const.INIT);
                nodeService.update(nodeInstance);
            }
        }
    }

}
