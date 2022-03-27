package finley.gmair.converter;

import finley.gmair.dto.knowledgebase.CommentDTO;
import finley.gmair.model.knowledgebase.Comment;
import finley.gmair.util.TimeUtil;


public class CommentConverter {
    public static Comment DTO2model(CommentDTO commentDTO) {
        if (commentDTO == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setKnowledgeId(commentDTO.getKnowledgeId());
        comment.setContent(commentDTO.getContent());
        comment.setStatus(commentDTO.getStatus());
        try {
            comment.setCreateTime(TimeUtil.formatTimeToDatetime(commentDTO.getCreateTime()));
            comment.setSolveTime(TimeUtil.formatTimeToDatetime(commentDTO.getSolveTime()));
        }catch (Exception e){
            e.printStackTrace();
        }
        comment.setResponserId(commentDTO.getResponserId());
        comment.setType(commentDTO.getType());
        return comment;
    }

    public static CommentDTO model2DTO(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setKnowledgeId(comment.getKnowledgeId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setStatus(comment.getStatus());
        commentDTO.setCreateTime(TimeUtil.datetimeToString(comment.getCreateTime()));
        commentDTO.setSolveTime(TimeUtil.datetimeToString(comment.getSolveTime()));
        commentDTO.setResponserId(comment.getResponserId());
        commentDTO.setType(comment.getType());
        return commentDTO;
    }
}
