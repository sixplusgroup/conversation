package finley.gmair.dao;


import finley.gmair.model.knowledgebase.Tag;
import finley.gmair.model.knowledgebase.TagRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagRelationMapper {

    List<TagRelation> getByKnowledgeId(@Param("knowledge_id") Integer id);

    List<TagRelation> getByTagId(@Param("tag_id") Integer id);

    void deleteAllByKnowledgeId(@Param("id") Integer id);

    void insert(TagRelation tagRelation);

    void delete(TagRelation tagRelation);
}
