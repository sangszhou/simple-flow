package com.example.domain;

import com.example.typehandler.ListTypeHandler;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@Table(name = "node_instance")
public class NodeInstance extends BaseEntity {
    @Column(name = "parent_flow_id")
    long parentFlowId;
    // id 是标记
    @Column(name = "node_id")
    String nodeId;
    String name;
    // task, flow, parallel, switch
    @Column(name = "node_type")
    String nodeType;
    // -1 invalid, 0 -> init, 1 -> running, 2 -> failed, 3 -> success, 4 -> skipped
    @Column(name = "node_status")
    int nodeStatus = -1;
    @Column(name = "class_name")
    String className;
    @Column(name = "arg")
    String arg;
    @Column(name = "output")
    String output;
    @Column(name = "pre_node")
    @ColumnType(typeHandler = ListTypeHandler.class)
    List<String> preNode;

    public void addPreNode(String id) {
        if (preNode == null) {
            preNode = new LinkedList<>();
        }
        preNode.add(id);
    }

}
