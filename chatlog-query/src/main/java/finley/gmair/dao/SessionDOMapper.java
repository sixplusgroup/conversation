package finley.gmair.dao;

import org.apache.ibatis.annotations.Param;

public interface SessionDOMapper {
    int insertSession(@Param("id") String originalSessionId);
}
