package finley.gmair.controller;


import finley.gmair.converter.CommentPagerConverter;
import finley.gmair.dto.knowledgebase.CommentPagerDTO;
import finley.gmair.enums.knowledgeBase.CommentStatus;
import finley.gmair.service.CommentService;
import finley.gmair.util.ResultData;
import finley.gmair.utils.PageParam;
import finley.gmair.vo.knowledgebase.CommentPagerVO;
import finley.gmair.vo.knowledgebase.KnowledgePagerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 反馈处理类
 * @Author great fish
 * @Date 2022/3/30 21:49
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    /**
     * @Description 根据状态分页获取评论列表
     * @Author great fish
     * @Date 21:49 2022/3/30
     */
    @GetMapping("/getCommentListByStatus")
    public ResultData getCommentListByStatus(@RequestParam String status, @RequestBody PageParam pageParam){
        int statusCode = CommentStatus.getCodeByValue(status);
        CommentPagerDTO commentPagerDTO = commentService.getCommentListByStatus(statusCode,pageParam.getPageNum(), pageParam.getPageSize());
        CommentPagerVO commentPagerVO = CommentPagerConverter.DTO2VO(commentPagerDTO);
        return ResultData.ok(commentPagerVO,null);
    }
}
