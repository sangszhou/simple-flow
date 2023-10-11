package com.example.domain;

import lombok.Getter;

@Getter
public enum NodeTypeEnum {
    Flow("Flow"),
    Task("Task"),
    Switch("Switch"),
    Parallel("Parallel");

    private String name;

    NodeTypeEnum(String name) {
        this.name = name;
    }

}
