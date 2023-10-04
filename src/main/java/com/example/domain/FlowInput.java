package com.example.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class FlowInput {
    String operator;
    Map<String, String> input;
}
