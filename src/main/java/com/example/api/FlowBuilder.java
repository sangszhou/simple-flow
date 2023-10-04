package com.example.api;

import com.example.domain.FlowInput;
import com.example.domain.NodeDefinition;

public interface FlowBuilder {
    NodeDefinition build(FlowInput flowInput);
}
