package finley.gmair.service;

import finley.gmair.dto.knowledgebase.CommentDTO;
import finley.gmair.dto.knowledgebase.CommentPagerDTO;
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

    /**
     * @Description 插入一条评论
     * @Author great fish
     * @Date 15:52 2022/4/16
     */
    void insertComment(CommentDTO commentDTO);

    /**
     * @Description 根据id废弃一条评论
     * @Author great fish
     * @Date 16:27 2022/4/16
     */
    void abandonComment(Integer commentId);

    /**
     * @Description 用户根据状态获得自己的评论列表
     * @Author great fish
     * @Date 16:54 2022/4/16
     */
    List<CommentDTO> getUserCommentListByStatus(int status,int userId);
}
