package com.example.demo.app_lifecycle.task;

import com.example.annotation.Input;
import com.example.api.TaskBuilder;
import com.example.api.TaskResult;
import com.example.domain.NodeStatusEnum;
import com.example.util.Const;
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
        if (ecsIpList != null) {
            logger.info("bind ecs: |{}| to lb: |{}|",
                    ecsIpList.stream().reduce((a, b) -> a + "->" + b).orElse("null"), lb);
        }
        return TaskResult.builder()
                .status(Const.SUCCESS)
                .build();
    }
}
