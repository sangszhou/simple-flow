package com.example.demo.app_lifecycle.task;

import com.example.annotation.Input;
import com.example.api.TaskBuilder;
import com.example.api.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BindEcsToLbTask implements TaskBuilder {
    private Logger logger = LoggerFactory.getLogger(BindEcsToLbTask.class);

    @Input
    List<String> ecsIpList;
    @Input
    String lb;

    @Override
    public TaskResult execute() {
        logger.info("bind ecs: |{}| to lb: |{}|",
                ecsIpList.stream().reduce((a, b) -> a + "->" + b), lb);
        return TaskResult.builder()
                .status(2)
                .build();
    }
}
