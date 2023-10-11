package com.example.domain;

import com.example.typehandler.ListTypeHandler;
import com.example.typehandler.NodeStatusTypeHandler;
import com.example.typehandler.NodeTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Table(name = "node_instance")
public class NodeInstance {
    public NodeInstance() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "create_time")
//    @JsonFormat(timezone = "Asia/Shanghai", pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createTime;
//
//    @Column(name = "update_time")
//    @JsonFormat(timezone = "Asia/Shanghai", pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime updateTime;

//    @Column(name = "del")
//    private Boolean del;

    @Column(name = "parent_flow_id")
    Long parentFlowId;
    // id 是标记
    @Column(name = "node_id")
    String nodeId;
    String name;
    // task, flow, parallel, switch
    @Column(name = "node_type")
//    @ColumnType(typeHandler = NodeTypeHandler.class)
    String nodeType;
    // -1 invalid, 0 -> init, 1 -> running, 2 -> failed, 3 -> success, 4 -> skipped
    @Column(name = "node_status")
//    @ColumnType(typeHandler = NodeStatusTypeHandler.class)
    String nodeStatus;
    @Column(name = "class_name")
    String className;
    @Column(name = "arg")
    String arg;
    @Column(name = "output")
    String output;
    @Column(name = "pre_node")
//    @ColumnType(typeHandler = ListTypeHandler.class)
    String preNode;

    public void addPreNode(String id) {
        if (preNode == null) {
            preNode = id;
            return;
        }
        preNode = preNode + "," + id;
    }

}
