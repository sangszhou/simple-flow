package com.example.domain;

import lombok.*;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class FlowInput {
    public FlowInput() {
    }

    String operator;
    Map<String, String> input;
}
