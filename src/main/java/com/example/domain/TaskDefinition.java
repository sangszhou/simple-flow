package com.example.domain;

import com.example.api.TaskBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

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
    // 有些参数是给定的
//    Map<String, Object> arg;
    // 有些输出是制定的
    String output;

    Map<String, Object> detailArg;

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

    public Map<String, Object> getDetailedArg() {
        if (detailArg == null) {
            detailArg = new HashMap<>();
        }
        return detailArg;
    }

//    public TaskDefinition withClazz(Class<? extends TaskBuilder> tb) {
//        this.className = tb.getName();
//        return this;
//    }

    public TaskDefinition constArg(String key, Object value) {
        getDetailedArg().put(key, value);
        return this;
    }

}
