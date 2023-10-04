package com.example.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.LinkedList;
import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
public abstract class NodeDefinition {
    String id;
    String name;
    List<NodeDefinition> nextNode;

    public List<NodeDefinition> getNextNode() {
        if (nextNode == null) {
            nextNode = new LinkedList<>();
        }
        return this.nextNode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNextNode(List<NodeDefinition> nextNode) {
        this.nextNode = nextNode;
    }

    public NodeDefinition followBy(NodeDefinition nodeDefinition) {
        this.getNextNode().add(nodeDefinition);
        return nodeDefinition;
    }

    public SwitchDefinition conditional() {
        SwitchDefinition switchDefinition = SwitchDefinition.builder().build();
        this.getNextNode().add(switchDefinition);
        return switchDefinition;
    }

}
