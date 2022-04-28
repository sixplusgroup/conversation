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
    void insert(Tag tag);

    void delete(Integer tag_id);

    void modify(Tag tag);

    @Select("select * from tag")
    List<Tag> getAll();

    Tag getByName(@Param("tag_name") String tag_name);

    List<Tag> getTagsByKnowledge(Integer id);


}
