package com.example.demo.app_lifecycle.flow;

import com.example.api.FlowBuilder;
import com.example.demo.app_lifecycle.task.CreateAppTask;
import com.example.demo.app_lifecycle.task.CreateEnvTask;
import com.example.demo.app_lifecycle.task.CreateWorkloadTask;
import com.example.domain.FlowInput;
import com.example.domain.NodeDefinition;
import com.example.domain.TaskDefinition;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.LinkedList;
import java.util.List;

public class AppCreateFlow implements FlowBuilder {

    @Override
    public NodeDefinition build(FlowInput flowInput) throws JsonProcessingException {
        String appName = flowInput.getInput().get("appName");

        TaskDefinition createAppTask = TaskDefinition.builder()
                .name("创建应用")
                .clazz(CreateAppTask.class)
                .build()
                .constArg("appName", appName);

        List<String> envs = new LinkedList<>();
        envs.add("sit");
        envs.add("beta");
//        envs.add("prod");

        for (String env : envs) {
            // todo 无法创建赋值 id
            TaskDefinition createEnvTask = TaskDefinition.builder()
                    .name("创建环境")
                    .clazz(CreateEnvTask.class)
                    .build()
                    .constArg("appName", appName)
                    .constArg("env", env)
                    ;
            TaskDefinition createWorkloadTask = TaskDefinition.builder()
                    .name("创建部署组")
                    .clazz(CreateWorkloadTask.class)
                    .build()
                    .constArg("appName", appName)
                    .constArg("env", env)
                    .constArg("workload", "default")
                    ;
            createAppTask
                    .followBy(createEnvTask)
                    .followBy(createWorkloadTask);
        }

        return createAppTask;
    }
}
