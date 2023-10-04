package com.example.demo.app_lifecycle.task;

import com.example.annotation.Input;
import com.example.api.TaskBuilder;
import com.example.api.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateEnvTask implements TaskBuilder {
    private Logger logger = LoggerFactory.getLogger(CreateEnvTask.class);

    @Input
    String appName;
    @Input
    String env;

    @Override
    public TaskResult execute() {
        logger.info("create env, appName:|{}|, env:|{}|", appName, env);
        return TaskResult.builder()
                .status(2)
                .build();
    }
}
