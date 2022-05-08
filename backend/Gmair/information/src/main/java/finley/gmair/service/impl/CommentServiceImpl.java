package finley.gmair.service.impl;

import finley.gmair.converter.CommentConverter;
import finley.gmair.dao.knowledgebase.CommentMapper;
import finley.gmair.dto.knowledgebase.CommentDTO;
import finley.gmair.enums.knowledgeBase.CommentStatus;
import finley.gmair.model.knowledgebase.Comment;
import finley.gmair.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;

    @Override
    public List<CommentDTO> getCommentListByStatus(int status) {
        List<Comment> comments = commentMapper.getByStatus(status);
        return comments.stream().map(CommentConverter::model2DTO).collect(Collectors.toList());
    }

    @Override
    public void insertComment(CommentDTO commentDTO) {
        commentMapper.insert(CommentConverter.DTO2model(commentDTO));
    }

    @Override
    public void abandonComment(Integer commentId) {
        commentMapper.updateStatus(commentId, CommentStatus.ABANDON.getCode());
    }

    @Override
    public List<CommentDTO> getUserCommentListByStatus(int status, int userId) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("status", status);
        condition.put("responser_id", userId);
        List<Comment> comments = commentMapper.query(condition);
        return comments.stream().map(CommentConverter::model2DTO).collect(Collectors.toList());
    }
}
