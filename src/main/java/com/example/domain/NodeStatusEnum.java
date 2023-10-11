package com.example.domain;

import lombok.Getter;

@Getter
public enum NodeStatusEnum {
    INVALID("INVALID"),
    INIT("INIT"),
    RUNNING("RUNNING"),
    SUCCESS("SUCCESS"),
    FAILED("FAILED"),
    SKIPPED("SKIPPED");

    String name;

    NodeStatusEnum(String name) {
        this.name = name;
    }

}
