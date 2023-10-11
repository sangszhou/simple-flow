package com.example.demo.app_lifecycle.flow;

import com.example.annotation.Input;
import com.example.api.FlowBuilder;
import com.example.api.TaskBuilder;
import com.example.demo.app_lifecycle.task.DeleteAppTask;
import com.example.demo.app_lifecycle.task.NotifyTask;
import com.example.demo.app_lifecycle.task.UserTask;
import com.example.domain.FlowInput;
import com.example.domain.NodeDefinition;
import com.example.domain.TaskDefinition;
import com.fasterxml.jackson.core.JsonProcessingException;

public class AppDeleteFlow implements FlowBuilder {

    @Override
    public NodeDefinition build(FlowInput flowInput) throws JsonProcessingException {
        String appName = flowInput.getInput().get("appName");

        TaskDefinition userTask = TaskDefinition.builder()
                .clazz(UserTask.class)
                .build()
                .constArg("candidate", "qima");

        TaskDefinition deleteApp = TaskDefinition.builder()
                .clazz(DeleteAppTask.class)
                .name("deleteApp")
                .build()
                .constArg("appName", appName);

        TaskDefinition skipDeleteNotifyUser = TaskDefinition.builder()
                .clazz(NotifyTask.class)
                .name("skipDelete")
                .build()
                .constArg("notifyUser", flowInput.getOperator())
                .constArg("result", "skipped");

        TaskDefinition deletedNotifyUser = TaskDefinition.builder()
                .clazz(NotifyTask.class)
                .name("deleted")
                .build()
                .constArg("notifyUser", flowInput.getOperator())
                .constArg("result", "executed");

        userTask.conditional()
                .when("success", deleteApp)
                .missingMatch(skipDeleteNotifyUser);

        // todo 如果让 notifyUser 感知到 deleteApp 是否执行了，要怎么传递呢？
        // 一种办法是创建两个 notifyUser

        deleteApp
                .followBy(deletedNotifyUser);

        return userTask;
    }
}
