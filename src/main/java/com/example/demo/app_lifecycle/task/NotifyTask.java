package com.example.demo.app_lifecycle.task;

import com.example.annotation.Input;
import com.example.api.TaskBuilder;
import com.example.api.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyTask implements TaskBuilder {
    private Logger logger = LoggerFactory.getLogger(NotifyTask.class);

    @Input
    String notifyUser;
    @Input
    String result;

    @Override
    public TaskResult execute() {
        logger.info("notify: |{}|, result: |{}|", notifyUser, result);
        return TaskResult.builder()
                .status(2)
                .build();
    }
}
