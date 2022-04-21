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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("knowledge-base/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @Autowired
    KnowledgeService knowledgeService;


    @PreAuthorize("hasAuthority('comment_create')")
    @PostMapping("/create")
    /**
     *@Description 管理人员/用户创建一条评论
     *@Author great fish
     *@Date  2022/4/21
     *@param comment knowledgeId、content、responserId、type必填。
     *
     * @return {@link ResultData }
     */

    public ResultData comment(@RequestBody CommentVO comment) {
        CommentDTO commentDTO = CommentConverter.VO2DTO(comment);
        if(comment.getType().equals(CommentType.AUDITORS.getValue())){//仅有管理人员反馈改变知识状态为待编辑
            knowledgeService.reedit(commentDTO.getKnowledgeId());
        }
        commentService.insertComment(commentDTO);
        return ResultData.ok(null);
    }

    @PreAuthorize("hasAuthority('comment_modify')")
    @PostMapping("/correct/{id}")
    /**
     *@Description 知识采编者对某条知识进行纠错
     *@Author great fish
     *@Date  2022/4/21
     *@param commentId 所针对的评论id
     * @param knowledgeVO 知识必传id，title,content，modifyTime
     *
     * @return {@link ResultData }
     */

    public ResultData correct(@PathVariable Integer commentId, @RequestBody KnowledgeVO knowledgeVO) {
        KnowledgeDTO knowledgeDTO= KnowledgeConverter.VO2DTO(knowledgeVO);
        knowledgeDTO.setStatus(KnowledgeStatus.PENDING_REVIEW.getCode());
        knowledgeService.correct(knowledgeDTO,commentId);
        return ResultData.ok(null);
    }

    @PostMapping("abandon/{id}")
    @PreAuthorize("hasAuthority('comment_modify')")
    /**
     *@Description 采编人员废弃一条评论
     *@Author great fish
     *@Date  2022/4/21
     *@param id commentId
     *
     * @return {@link ResultData }
     */

    public ResultData abandon(@PathVariable Integer id){
        commentService.abandonComment(id);
        return ResultData.ok(null);
    }

    @PreAuthorize("hasAuthority('comment_getAll')")
    @PostMapping("getCommentListByStatus/{status}")
    /**
     *@Description 采编人员根据评论状态分页获得评论列表
     *@Author great fish
     *@Date  2022/4/21
     *@param status 评论状态，枚举为待解决、已解决、废弃
     * @param pageParam 分页参数
     *
     * @return {@link ResultData }
     */

    public ResultData getCommentListByStatus(@PathVariable String status, @RequestBody PageParam pageParam){
        CommentPagerDTO commentPagerDTO = commentService.getCommentListByStatus(CommentStatus.getCodeByValue(status),pageParam.getPageNum(),pageParam.getPageSize());
        return ResultData.ok(CommentPagerConverter.DTO2VO(commentPagerDTO),null);
    }

    @PreAuthorize("hasAuthority('comment_getOwn')")
    @PostMapping("getUserCommentListByStatus")
    /**
     *@Description 用户根据评论状态获得自己的评论列表
     *@Author great fish
     *@Date  2022/4/21
     *@param userId 用户id
     * @param status 评论状态，枚举为待解决、已解决、废弃
     *
     * @return {@link ResultData }
     */

    public ResultData getUserCommentListByStatus(@RequestParam("userId") Integer userId,@RequestParam("status") String status){
        List<CommentDTO> commentDTOS= commentService.getUserCommentListByStatus(
                CommentStatus.getCodeByValue(status),userId);
        return ResultData.ok(commentDTOS.stream().map(CommentConverter::DTO2VO).collect(Collectors.toList()),null);
    }


}
