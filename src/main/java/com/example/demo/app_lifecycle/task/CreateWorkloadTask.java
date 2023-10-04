package com.example.demo.app_lifecycle.task;

import com.example.annotation.Input;
import com.example.api.TaskBuilder;
import com.example.api.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateWorkloadTask implements TaskBuilder {
    private Logger logger = LoggerFactory.getLogger(CreateWorkloadTask.class);

    @Input
    String appName;
    @Input
    String env;
    @Input
    String workload;

    @Override
    public TaskResult execute() {
        logger.info("create workload, appName:|{}|, env:|{}|, workload:|{}|",
                appName, env, workload);
        return TaskResult.builder()
                .status(2)
                .build();
    }

}
