package com.example.demo.app_lifecycle.task;

import com.example.annotation.Input;
import com.example.api.TaskBuilder;
import com.example.api.TaskResult;
import com.example.domain.NodeStatusEnum;
import com.example.util.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateAppTask implements TaskBuilder {
    private Logger logger = LoggerFactory.getLogger(CreateAppTask.class);

    @Input
    String appName;

    @Override
    public TaskResult execute() {
        logger.info("create app: |{}|", appName);

        return TaskResult
                .builder()
                .status(Const.SUCCESS)
                .build();
    }
}
