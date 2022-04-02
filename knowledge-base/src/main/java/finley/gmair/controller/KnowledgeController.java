package finley.gmair.controller;

import finley.gmair.converter.CommentConverter;
import finley.gmair.converter.KnowledgeConverter;
import finley.gmair.dto.knowledgebase.CommentDTO;
import finley.gmair.dto.knowledgebase.KnowledgeDTO;
import finley.gmair.enums.knowledgeBase.KnowledgeStatus;
import finley.gmair.model.knowledgebase.Knowledge;
import finley.gmair.service.CommentService;
import finley.gmair.service.KnowledgeService;
import finley.gmair.util.ResultData;
import finley.gmair.utils.PageParam;
import finley.gmair.vo.knowledgebase.CommentVO;
import finley.gmair.vo.knowledgebase.KnowledgePagerVO;
import finley.gmair.vo.knowledgebase.KnowledgeVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    @Autowired
    KnowledgeService knowledgeService;

    /**
     * 审核者审核通过，发布知识
     * @param id
     * @return
     *
     */
    @GetMapping("/publish/{id}")
    public ResultData publish(@PathVariable Integer id) {
        knowledgeService.publish(id);
        return ResultData.ok(null);
    }

    /**
     * 管理员审核不通过，反馈评论
     * @param id
     * @param comment
     * @return
     */
    @PostMapping("/reedit/{id}")
    public ResultData reedit(@PathVariable Integer id, @RequestBody CommentVO comment) {
        CommentDTO commentDTO = CommentConverter.VO2DTO(comment);
        knowledgeService.reedit(id,commentDTO);
        return ResultData.ok(null);
    }

    /**
     * 分页获取所有知识
     * @param pageParam
     * @return
     *
     */
    @PostMapping("/getPage")
    public ResultData getPage(@RequestBody PageParam pageParam) {
        KnowledgePagerVO knowledgeList = knowledgeService.getPage(pageParam.getPageNum(), pageParam.getPageSize());
        return ResultData.ok(knowledgeList, null);
    }

    /**
     * 分页获取"待审核"状态的知识
     * @param pageParam
     * @return
     *
     */
    @PostMapping("/getAuditPage")
    public ResultData getAuditPage(@RequestBody PageParam pageParam) {
        KnowledgePagerVO knowledgeList = knowledgeService.getAuditPage(pageParam.getPageNum(), pageParam.getPageSize());
        return ResultData.ok(knowledgeList, null);
    }

    /**
     * 根据类型，分页获取知识
     * @param id
     * @param pageParam
     * @return
     */
//    @PostMapping("/getPageByType/{id}")
//    public ResultData getPageByType(@PathVariable Integer id, @RequestBody PageParam pageParam) {
//        KnowledgePagerVO knowledgePagerList = knowledgeService.getPageByType(id, pageParam.getPageNum(), pageParam.getPageSize());
//        return ResultData.ok(knowledgePagerList, null);
//    }

    /**
     * 删除某条知识
     * @param id
     * @return
     *
     */
    @GetMapping("/delete/{id}") //done
    public ResultData delete(@PathVariable Integer id) {
        knowledgeService.delete(id);
        return ResultData.ok(null);
    }

    /**
     * 创建知识
     * @param knowledgeVO
     * @return
     *
     */
    @PostMapping("/create") //done
    public ResultData create(@RequestBody KnowledgeVO knowledgeVO) {
        knowledgeService.create(knowledgeVO);
        return ResultData.ok(null);
    }

    /**
     * 根据ID获取某条知识
     * @param id
     * @return
     *
     */
    @PostMapping("/getById/{id}")
    public ResultData getById(@PathVariable Integer id) {
        KnowledgeVO knowledgeVO = knowledgeService.getById(id);
        return ResultData.ok(knowledgeVO, null);
    }

    /**
     * 修改某条知识(需要提供知识ID, title, content)
     * @param knowledgeVO
     * @return
     *
     */
    @PostMapping("/modify")
    public ResultData modify(@RequestBody KnowledgeVO knowledgeVO){
        knowledgeService.modify(knowledgeVO);
        return ResultData.ok(null);
    }

    /**
     * 根据搜索关键字，对标题和内容进行全文搜索
     * @param key
     * @return
     *
     */
    @PostMapping("/fulltext_search")
    public ResultData fulltextSearch(@RequestBody String key) {
        List<Knowledge> knowledges = knowledgeService.fulltextSearch(key);
        List<KnowledgeVO> knowledgeVOs = knowledges.stream().map(KnowledgeConverter::model2VO).collect(Collectors.toList());
        return ResultData.ok(knowledgeVOs, null);
    }

    /**
     * 知识使用者对某条知识进行纠错
     * @param commentId
     * @param knowledgeVO
     * @return
     */
    @PostMapping("/correct")
    public ResultData correct(@RequestParam Integer commentId, @RequestBody KnowledgeVO knowledgeVO) {
        KnowledgeDTO knowledgeDTO= KnowledgeConverter.VO2DTO(knowledgeVO);
        knowledgeDTO.setStatus(KnowledgeStatus.PENDING_REVIEW.getCode());
        knowledgeService.correct(knowledgeDTO,commentId);
        return ResultData.ok(null);
    }

}