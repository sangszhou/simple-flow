package com.example.api;

import com.example.domain.FlowInput;
import com.example.domain.NodeDefinition;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface FlowBuilder {
    NodeDefinition build(FlowInput flowInput) throws JsonProcessingException;
}
