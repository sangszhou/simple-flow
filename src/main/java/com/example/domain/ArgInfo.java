package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ArgInfo {
    public ArgInfo() {

    }

    String key;
    Object value;
    String type;
}
