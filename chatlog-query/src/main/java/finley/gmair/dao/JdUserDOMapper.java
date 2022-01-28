package finley.gmair.dao;

import org.apache.ibatis.annotations.Param;

public interface JdUserDOMapper {
    Integer getUserIdByName(@Param("name") String userName);
}
