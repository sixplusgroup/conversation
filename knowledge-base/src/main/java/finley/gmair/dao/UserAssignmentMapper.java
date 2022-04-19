package finley.gmair.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserAssignmentMapper {
    void insert(@Param("uid") Integer uid, @Param("rid")Integer rid);
}
