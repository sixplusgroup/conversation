package finley.gmair.dao;

import finley.gmair.model.knowledgebase.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {
    void insert(Comment comment);

    void updateStatus(int id,int status);

    List<Comment> getByStatus(int status);
}
