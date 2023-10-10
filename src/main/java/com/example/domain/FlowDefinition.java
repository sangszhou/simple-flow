package com.example.domain;

import com.example.api.FlowBuilder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

import java.util.Random;

@Data
@SuperBuilder(toBuilder = true)
public class FlowDefinition extends NodeDefinition {
    // 英文名
    String flowId;
    // 中文名
    String flowName;
    Class<? extends FlowBuilder> clazz;
    // todo flowInput 放这里合适吗
    FlowInput flowInput;

    @Override
    public String getId() {
        if (StringUtils.isEmpty(flowId)) {
            flowId = clazz.getSimpleName() + ":" + new Random().nextInt(1000);
        }
        return flowId;
    }

    public String getName() {
        if (StringUtils.isEmpty(name)) {
            return clazz.getSimpleName();
        }
        return name;
    }

}
