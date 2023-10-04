package com.example.demo.app_lifecycle.task;

import com.example.annotation.Input;
import com.example.api.TaskBuilder;
import com.example.api.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpandWorkloadTask implements TaskBuilder  {
    private Logger logger = LoggerFactory.getLogger(ExpandWorkloadTask.class);

    @Input
    String appName;
    @Input
    Integer num;

    @Override
    public TaskResult execute() {
        logger.info("expand workload, app:|{}| num: |{}|", appName, num);
        return TaskResult.builder()
                .status(2)
                .build();
    }
}
