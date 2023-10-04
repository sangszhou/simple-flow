package com.example.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArgInfo {
    String key;
    String value;
    String type;
}
