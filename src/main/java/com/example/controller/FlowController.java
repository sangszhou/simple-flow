package com.example.controller;

import com.example.api.RestResponse;
import com.example.domain.FlowInput;
import com.example.runner.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FlowController {
    @Autowired
    FlowService flowService;

//    @PostMapping("startFlow")
//    public RestResponse<Long> startFlow(@RequestBody FlowInput flowInput,
//                                        @RequestParam("flowName") String flowName) {
//        try {
//        long id = flowService.startFlow(flowName, flowInput);
//
//    }
}
