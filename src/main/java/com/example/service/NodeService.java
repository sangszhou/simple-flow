package com.example.service;

import com.example.domain.NodeInstance;
import com.example.mapper.NodeInstanceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeService {

    @Autowired
    NodeInstanceRepo nodeInstanceRepo;

    public int create(NodeInstance nodeInstance) {
        return nodeInstanceRepo.insert(nodeInstance);
    }

    public int update(NodeInstance nodeInstance) {
        return nodeInstanceRepo.updateByPrimaryKeySelective(nodeInstance);
    }

    public List<NodeInstance> findNode(NodeInstance query) {
        return nodeInstanceRepo.selectByExample(query);
    }

}
