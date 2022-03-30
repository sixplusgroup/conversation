package finley.gmair.service;

import finley.gmair.dto.knowledgebase.CommentPagerDTO;
import finley.gmair.model.knowledgebase.Comment;
import finley.gmair.utils.PageParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {

    /**
     * @Description 根据状态分页取反馈
     * @Author great fish
     * @Date 21:57 2022/3/30
     */
    CommentPagerDTO getCommentListByStatus(int status, Integer pageNum, Integer pageSize);
}
