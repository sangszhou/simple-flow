package com.example.demo.app_lifecycle.controller;

import com.example.api.RestResponse;
import com.example.demo.app_lifecycle.flow.AppCreateFlow;
import com.example.demo.app_lifecycle.flow.AppDeleteFlow;
import com.example.domain.FlowInput;
import com.example.runner.FlowService;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppLifecycleController {
    private Logger logger = LoggerFactory.getLogger(AppLifecycleController.class);

    @Autowired
    FlowService flowService;

    @PostMapping("createApp")
    public RestResponse<Long> createApp(@RequestParam String appName,
                                        @RequestParam String operator) {
        FlowInput flowInput = FlowInput.builder()
                .operator(operator)
                .input(ImmutableMap.of("appName", appName))
                .build();
        try {
            long id = flowService.startFlow(AppCreateFlow.class, flowInput);
            return RestResponse.<Long>builder()
                    .code(200)
                    .data(id)
                    .build();
        } catch (Exception e) {
            logger.error("failed to create app", e);
            return RestResponse.<Long>builder()
                    .code(500)
                    .message(e.getMessage())
                    .build();
        }
    }

    @PostMapping("deleteApp")
    public RestResponse<Long> deleteApp(@RequestParam String appName,
                                        @RequestParam String operator) {
        FlowInput flowInput = FlowInput.builder()
                .operator(operator)
                .input(ImmutableMap.of("appName", appName))
                .build();
        try {
            long id = flowService.startFlow(AppDeleteFlow.class, flowInput);
            return RestResponse.<Long>builder()
                    .code(200)
                    .data(id)
                    .build();
        } catch (Exception e) {
            logger.error("failed to delete app: |{}|", appName, e);
            return RestResponse.<Long>builder()
                    .code(500)
                    .message(e.getMessage())
                    .build();
        }
    }
}
