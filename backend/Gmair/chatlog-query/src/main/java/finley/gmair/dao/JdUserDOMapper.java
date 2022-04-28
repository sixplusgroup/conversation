package finley.gmair.dao;

import finley.gmair.model.chatlog.usr.jd.JingdongUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface JdUserDOMapper {
    @Select("select usr_id from usr_jd where usr_name=#{name}")
    Integer getUserIdByName(@Param("name") String userName);

    @Insert("insert into usr_jd set usr_name=#{user.name}")
//    @Options(useGeneratedKeys = true, keyProperty = "usr_id")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "user.id", before = false, resultType = Integer.class)
    Integer insertUser(@Param("user") JingdongUser user);
}
