package finley.gmair.dao.knowledgebase;

import finley.gmair.model.knowledgebase.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommentMapper {
    void insert(Comment comment);

    void updateStatus(@Param("id") int id, @Param("status") int status);

    List<Comment> getByStatus( @Param("status")int status);

    List<Comment> query(Map<String, Object> condition);
}
