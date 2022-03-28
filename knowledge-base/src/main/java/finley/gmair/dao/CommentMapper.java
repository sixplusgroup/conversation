package finley.gmair.dao;

import finley.gmair.model.knowledgebase.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CommentMapper {
    void insert(Comment comment);
}
