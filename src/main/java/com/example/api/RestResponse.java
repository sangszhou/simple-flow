package com.example.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestResponse<T> {
    int code = 200;
    String message;
    T data;
}
