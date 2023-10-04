package com.example.runner;

import com.example.domain.NodeInstance;
import com.example.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

public class FlowRunner {
    @Autowired
    NodeService nodeService;

    public void initFlow(NodeInstance flowNode) {

    }

    public List<NodeInstance> findInitingFlow() {
        return new LinkedList<>();
    }

}
