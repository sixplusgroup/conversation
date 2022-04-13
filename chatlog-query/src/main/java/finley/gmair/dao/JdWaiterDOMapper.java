package finley.gmair.dao;

import finley.gmair.model.chatlog.usr.jd.JingdongWaiter;
import org.apache.ibatis.annotations.*;

@Mapper
public interface JdWaiterDOMapper {
    @Select("select waiter_id from waiter_jd where waiter_name=#{name}")
    Integer getWaiterIdByName(@Param("name") String waiterName);

    @Insert("insert into waiter_jd set waiter_name=#{waiter.name}")
//    @Options(useGeneratedKeys = true, keyProperty = "waiter_id")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "waiter.id", before = false, resultType = Integer.class)
    Integer insertWaiter(@Param("waiter") JingdongWaiter waiter);
}
