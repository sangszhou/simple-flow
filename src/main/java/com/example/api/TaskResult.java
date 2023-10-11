package com.example.api;

import com.example.domain.NodeStatusEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResult {
    // -1 -> invalid, 0 -> init, 1 -> running, 2 -> success, 3 -> fail, 5 -> skipped
    String status;
    String msg;
}
