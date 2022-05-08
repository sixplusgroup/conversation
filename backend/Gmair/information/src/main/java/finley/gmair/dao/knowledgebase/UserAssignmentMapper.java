package finley.gmair.dao.knowledgebase;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAssignmentMapper {
    void insert(@Param("uid") Integer uid, @Param("rid")Integer rid);
}
