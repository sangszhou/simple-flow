package com.example.service;

import com.example.domain.NodeInstance;
import com.example.mapper.NodeInstanceRepo;
import com.example.util.Const;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class NodeService {
    private Logger logger = LoggerFactory.getLogger(NodeService.class);

    @Autowired
    NodeInstanceRepo nodeInstanceRepo;

    public int create(NodeInstance nodeInstance) {
        return nodeInstanceRepo.insert(nodeInstance);
    }

    public int update(NodeInstance nodeInstance) {
        return nodeInstanceRepo.updateByPrimaryKeySelective(nodeInstance);
    }

    public List<NodeInstance> findNode(NodeInstance query) {
        return nodeInstanceRepo.select(query);
    }

    public List<NodeInstance> findNoPredecessorNode(NodeInstance query) {
        Example example = new Example(NodeInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentFlowId", query.getId());
        criteria.andIsNull("preNode");
        List<NodeInstance> result = nodeInstanceRepo.selectByExample(example);
        return result;
    }

    public List<NodeInstance> tryFindNextNodeToTrigger(long flowId, String predecessorName) {
        Example example = new Example(NodeInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentFlowId", flowId);
        criteria.andLike("preNode", "%" + predecessorName + "%");
        List<NodeInstance> result = nodeInstanceRepo.selectByExample(example);
        return result;
    }

    public List<NodeInstance> tryFinishParentNode(long flowId) {
        Example example = new Example(NodeInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentFlowId", flowId);
        criteria.andNotIn("node_status", Lists.newArrayList(Const.SUCCESS));
        int cnt = nodeInstanceRepo.selectCountByExample(example);
        if (cnt == 0) {
            logger.info("try finish parent node");
//            NodeInstance flowInst = nodeInstanceRepo.selectByPrimaryKey(flowId);
//            flowInst.setNodeStatus(Const.SUCCESS);
//            if (flowInst.getParentFlowId() != null) {
//                this.tryFindNextNodeToTrigger(flowInst.getParentFlowId())
//            }
        }
        return null;
    }

}
