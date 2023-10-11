package com.example.runner;

import com.example.domain.NodeInstance;
import com.example.domain.NodeStatusEnum;
import com.example.service.NodeService;
import com.example.util.Const;
import com.example.util.JsonHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sun.nio.sctp.NotificationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class SwitchNodeRunner {
    private Logger logger = LoggerFactory.getLogger(SwitchNodeRunner.class);

    @Autowired
    NodeService nodeService;

    public void initSwitchNode(NodeInstance switchNode) throws JsonProcessingException {
        String conditionStr = switchNode.getArg();
        Map<String, String> conditionMap = JsonHelper.getMapper().readValue(conditionStr, new TypeReference<Map<String, String>>() {});
        String targetId = null;
        for (String key : conditionMap.keySet()) {
            if (evaluateCondition(key)) {
                targetId = conditionMap.get(key);
                break;
            }
        }

        if (conditionMap.containsKey(Const.SwitchDefault) && StringUtils.isEmpty(targetId)) {
            targetId = conditionMap.get(Const.SwitchDefault);
        }

        NodeInstance targetNodeQueryParam = NodeInstance.builder()
                .nodeId(targetId)
                .build();

        List<NodeInstance> nodeInstance = nodeService.findNode(targetNodeQueryParam);
        if (nodeInstance.size() != 1) {
            logger.error("switch target node size not 1");
            return;
        }

        NodeInstance targetNode = nodeInstance.get(0);
        if (targetNode.getNodeStatus().equals(Const.INVALID)) {
            // invalid -> init
            targetNode.setNodeStatus(Const.INIT);
            nodeService.update(targetNode);
        } else {
            logger.error("target node status is not |invalid|, but: |{}|", targetNode.getNodeStatus());
        }
        // 其他非 targetNode 全部置为 skipped
        for (String nodeId : conditionMap.values()) {
            if (nodeId.equalsIgnoreCase(targetId)) {
                continue;
            }
            NodeInstance nodeQueryParam = NodeInstance.builder()
                    .parentFlowId(switchNode.getParentFlowId())
                    .nodeId(nodeId)
                    .build();
            nodeInstance = nodeService.findNode(nodeQueryParam);
            if (nodeInstance.size() != 1) {
                logger.error("failed to update node instance");
            }

            NodeInstance theNode = nodeInstance.get(0);
            theNode.setNodeStatus(Const.SKIP);
            nodeService.update(theNode);
        }

        switchNode.setNodeStatus(Const.SUCCESS);
        nodeService.update(switchNode);
    }

    /**
     * 这里的判断还比较麻烦，我们可以先支持 #.# = true or #.# = false 的解析格式
     * @param condition
     * @return
     */
    public boolean evaluateCondition(String condition) {
        if (condition.equals("success")) {
            return true;
        }
        return false;
    }
}
