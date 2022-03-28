package finley.gmair.controller;

import finley.gmair.converter.CommentConverter;
import finley.gmair.dto.knowledgebase.CommentDTO;
import finley.gmair.model.knowledgebase.Knowledge;
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

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    @Autowired
    KnowledgeService knowledgeService;

    /**
     * 审核者审核通过，发布知识
     * @param id
     * @return
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
    @PostMapping("/getPageByType/{id}")
    public ResultData getPageByType(@PathVariable Integer id, @RequestBody PageParam pageParam) {
        KnowledgePagerVO knowledgePagerList = knowledgeService.getPageByType(id, pageParam.getPageNum(), pageParam.getPageSize());
        return ResultData.ok(knowledgePagerList, null);
    }

    /**
     * 删除某条知识
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public ResultData delete(@PathVariable Integer id) {
        knowledgeService.delete(id);
        return ResultData.ok(null);
    }

    /**
     * 创建知识
     * @param knowledgeVO
     * @return
     */
    @PostMapping("/create")
    public ResultData create(@RequestBody KnowledgeVO knowledgeVO) {
        knowledgeService.create(knowledgeVO);
        return ResultData.ok(null);
    }

    /**
     * 根据ID获取某条知识
     * @param id
     * @return
     */
    @PostMapping("/getById/{id}")
    public ResultData getById(@PathVariable Integer id) {
        Knowledge knowledge = knowledgeService.getById(id);
        return ResultData.ok(knowledge, null);
    }

    /**
     * 修改某条知识(需要提供知识ID)
     * @param knowledgeVO
     * @return
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
     */
    @PostMapping("/fulltext_search")
    public ResultData fulltextSearch(@RequestBody String key) {
        List<Knowledge> knowledges = knowledgeService.fulltextSearch(key);
        return ResultData.ok(knowledges, null);
    }

    //todo 修改传底CommentVO,纠错后将comment状态改为已解决，将知识状态改为待审核（还是定义一个新状态，纠错后待审核）
    /**
     * 知识使用者对某条知识进行纠错
     * @param id
     * @param comment
     * @return
     */
    @PostMapping("/correct/{id}")
    public ResultData correct(@PathVariable Integer id, @RequestBody String comment) {
        knowledgeService.correct(id, comment);
        return ResultData.ok(null);
    }

    //todo 根据状态筛选获取评论列表
}