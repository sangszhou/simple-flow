package com.example.domain;

import lombok.experimental.SuperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * switch 不支持 followBy 操作
 */
@SuperBuilder(toBuilder = true)
public class SwitchDefinition extends NodeDefinition{
    private Logger logger = LoggerFactory.getLogger(SwitchDefinition.class);

    Map<String, NodeDefinition> conditionalInfo;

    public Map<String, NodeDefinition> getConditionalInfo() {
        if (this.conditionalInfo == null) {
            this.conditionalInfo = new HashMap<>();
        }
        return this.conditionalInfo;
    }

    @Override
    public NodeDefinition followBy(NodeDefinition nodeDefinition) {
        // 不支持 followBy 操作
        throw new RuntimeException();
    }


    public SwitchDefinition when(String condition, NodeDefinition nodeDefinition) {
        getConditionalInfo().put(condition, nodeDefinition);
        return this;
    }

    /**
     * missing match 作为最后一个 match 选项，失去命中以后，不再返回节点
     * @param nodeDefinition
     */
    public void missingMatch(NodeDefinition nodeDefinition) {
        getConditionalInfo().put("system_other", nodeDefinition);
    }

}
