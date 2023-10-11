package com.example.mapper;

import com.example.domain.NodeInstance;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface NodeInstanceRepo extends Mapper<NodeInstance> {
//    List<NodeInstance> getNodeInstanceByTypeAndStatus(@Param("nodeStatus") String nodeStatus, @Param("nodeType") String nodeType );

    @Select("select * from node_instance where parent_flow_id = #{flowId}")
    List<NodeInstance> findNoPredecessor(@Param("flowId") long flowId);

}
