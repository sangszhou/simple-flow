package com.example.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResult {
    // -1 -> invalid, 0 -> init, 1 -> running, 2 -> success, 3 -> fail, 5 -> skipped
    int status;
    String msg;
}
