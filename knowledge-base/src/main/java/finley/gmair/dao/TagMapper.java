package finley.gmair.dao;


import finley.gmair.model.knowledgebase.Knowledge;
import finley.gmair.model.knowledgebase.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagMapper {
    void insert(@Param("tag_name") String tag_name);

    void delete(@Param("tag_name") String tag_name);

    void modify(Tag tag);

    @Select("select * from tag")
    List<Tag> getAll();

    Tag getByName();

    List<Tag> getTagsByKnowledge(Integer id);


}
