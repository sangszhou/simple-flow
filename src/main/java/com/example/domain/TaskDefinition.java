package com.example.domain;

import com.example.api.TaskBuilder;
import com.example.util.JsonHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.ibatis.annotations.Arg;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Data
@SuperBuilder(toBuilder = true)
public class TaskDefinition extends NodeDefinition {
    String className;
    Class<? extends TaskBuilder> clazz;
    // 版本号, 可选值
    int version;
    int maxRetry;
    // 有些输出是制定的
    String output;

    Map<String, ArgInfo> detailArg;

    @Override
    public String getId() {
        if (id == null) {
            // todo 增加 simpleName
            id = getClazz().getSimpleName() + ":" + new Random().nextInt(1000);
        }
        return id;
    }

    public String getClassName() {
        if (className != null) {
            return className;
        }
        if (clazz != null) {
            return clazz.getName();
        }
        return null;
    }

    public Map<String, ArgInfo> getDetailedArg() {
        if (detailArg == null) {
            detailArg = new HashMap<>();
        }
        return detailArg;
    }

    public TaskDefinition constArg(String key, Object value) throws JsonProcessingException {
        getDetailedArg().put(key,
                ArgInfo.builder()
                        .type("const")
                        .key(key)
                        .value(value)
                .build());
        return this;
    }

    public TaskDefinition dynamicArg(String key, String valueDef) {
        getDetailedArg().put(key, ArgInfo.builder()
                .type("dynamic")
                .key(key)
                .value(valueDef)
                .build());
        return this;
    }

}
