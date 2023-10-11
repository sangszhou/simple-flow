package com.example.demo.app_lifecycle.task;

import com.example.annotation.Input;
import com.example.annotation.Output;
import com.example.api.TaskBuilder;
import com.example.api.TaskResult;
import com.example.util.Const;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

public class CreateEcsTask implements TaskBuilder {
    private Logger logger = LoggerFactory.getLogger(CreateEcsTask.class);

    @Input
    Integer num;
    @Output
    List<String> ecsIpList;

    @Override
    public TaskResult execute() {
        ecsIpList = Lists.newArrayList("1.1.1.1", "2.2.2.2");
        logger.info("create ecs task, num:|{}|, ecsSnList:{}", num,
                ecsIpList.stream().reduce((a, b) -> a + "," + b));
        return TaskResult.builder()
                .status(Const.SUCCESS)
                .build();
    }
}
