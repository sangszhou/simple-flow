package com.example.demo.app_lifecycle.task;

import com.example.annotation.Input;
import com.example.api.TaskBuilder;
import com.example.api.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteAppTask implements TaskBuilder {
    private Logger logger = LoggerFactory.getLogger(DeleteAppTask.class);

    @Input
    String appName;

    @Override
    public TaskResult execute() {
        logger.info("delete app task |{}|", appName);
        return TaskResult.builder()
                .status(2)
                .build();
    }
}
