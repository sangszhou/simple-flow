package com.example.mapper;

import com.example.domain.NodeInstance;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface NodeInstanceRepo extends Mapper<NodeInstance> {
//    List<NodeInstance> getNodeInstanceByTypeAndStatus(@Param("nodeStatus") String nodeStatus, @Param("nodeType") String nodeType );

}
