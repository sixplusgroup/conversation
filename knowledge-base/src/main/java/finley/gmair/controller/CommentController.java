package finley.gmair.controller;


import finley.gmair.converter.CommentConverter;
import finley.gmair.converter.CommentPagerConverter;
import finley.gmair.converter.KnowledgeConverter;
import finley.gmair.dto.knowledgebase.CommentDTO;
import finley.gmair.dto.knowledgebase.CommentPagerDTO;
import finley.gmair.dto.knowledgebase.KnowledgeDTO;
import finley.gmair.enums.knowledgeBase.CommentStatus;
import finley.gmair.enums.knowledgeBase.CommentType;
import finley.gmair.enums.knowledgeBase.KnowledgeStatus;
import finley.gmair.service.CommentService;
import finley.gmair.service.KnowledgeService;
import finley.gmair.util.ResultData;
import finley.gmair.utils.PageParam;
import finley.gmair.vo.knowledgebase.CommentVO;
import finley.gmair.vo.knowledgebase.KnowledgeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("knowledge-base/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @Autowired
    KnowledgeService knowledgeService;

    /**
     * 评论(管理员、使用者)
     * @param comment
     * @return
     */
    @PreAuthorize("hasAuthority('comment_create')")
    @PostMapping("/create")
    public ResultData comment(@RequestBody CommentVO comment) {
        CommentDTO commentDTO = CommentConverter.VO2DTO(comment);
        if(comment.getType().equals(CommentType.AUDITORS.getValue())){//仅有管理人员反馈改变知识状态为待编辑
            knowledgeService.reedit(commentDTO.getKnowledgeId());
        }
        commentService.insertComment(commentDTO);
        return ResultData.ok(null);
    }

    /**
     * 知识采编者对某条知识进行纠错
     * @param commentId
     * @param knowledgeVO
     * @return
     */
    @PreAuthorize("hasAuthority('comment_modify')")
    @PostMapping("/correct/{id}")
    public ResultData correct(@PathVariable Integer commentId, @RequestBody KnowledgeVO knowledgeVO) {
        KnowledgeDTO knowledgeDTO= KnowledgeConverter.VO2DTO(knowledgeVO);
        knowledgeDTO.setStatus(KnowledgeStatus.PENDING_REVIEW.getCode());
        knowledgeService.correct(knowledgeDTO,commentId);
        return ResultData.ok(null);
    }

    /**
     * @Description 采编人员废弃一条评论
     * @Author great fish
     * @Date 16:28 2022/4/16
     */
    @PostMapping("abandon/{id}")
    @PreAuthorize("hasAuthority('comment_modify')")
    public ResultData abandon(@PathVariable Integer id){
        commentService.abandonComment(id);
        return ResultData.ok(null);
    }

    /**
     * @Description 采编人员根据评论状态分页获得评论列表
     * @Author great fish
     * @Date 16:36 2022/4/16
     */
    @PreAuthorize("hasAuthority('comment_getAll')")
    @PostMapping("getCommentListByStatus/{status}")
    public ResultData getCommentListByStatus(@PathVariable String status, @RequestBody PageParam pageParam){
        CommentPagerDTO commentPagerDTO = commentService.getCommentListByStatus(CommentStatus.getCodeByValue(status),pageParam.getPageNum(),pageParam.getPageSize());
        return ResultData.ok(CommentPagerConverter.DTO2VO(commentPagerDTO),null);
    }

    /**
     * @Description 用户根据评论状态分页获得自己的评论列表
     * @Author great fish
     * @Date 17:00 2022/4/16
     */
    @PreAuthorize("hasAuthority('comment_getOwn')")
    @PostMapping("getUserCommentListByStatus/{userId}/{status}")
    public ResultData getUserCommentListByStatus(@PathVariable Integer userId,@PathVariable String status,@RequestBody PageParam pageParam){
        CommentPagerDTO commentPagerDTO = commentService.getUserCommentListByStatus(
                CommentStatus.getCodeByValue(status),userId,pageParam.getPageNum(),pageParam.getPageSize());
        return ResultData.ok(CommentPagerConverter.DTO2VO(commentPagerDTO),null);
    }


}
