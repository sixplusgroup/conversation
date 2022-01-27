package finley.gmair.dao;

import org.apache.ibatis.annotations.Param;

public interface JdWaiterDOMapper {
    Integer getWaiterIdByName(@Param("name") String waiterName);
}
