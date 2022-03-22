package finley.gmair.controller;

import finley.gmair.model.knowledgebase.Knowledge;
import finley.gmair.service.KnowledgeService;
import finley.gmair.util.ResultData;
import finley.gmair.utils.PageParam;
import finley.gmair.vo.knowledgebase.KnowledgePagerVO;
import finley.gmair.vo.knowledgebase.KnowledgeVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
@AllArgsConstructor
@Validated
public class KnowledgeController {
    @Autowired
    KnowledgeService knowledgeService;

    @GetMapping("/publish/{id}")
    public ResultData publish(@PathVariable Integer id) {
        knowledgeService.publish(id);
        return ResultData.ok(null);
    }

    @PostMapping("/reedit/{id}")
    public ResultData reedit(@PathVariable Integer id, @RequestBody String comment) {
        knowledgeService.reedit(id, comment);
        return ResultData.ok(null);
    }

    @PostMapping("/getPage")
    public ResultData getPage(@RequestBody PageParam pageParam) {
        KnowledgePagerVO knowledgeList = knowledgeService.getPage(pageParam.getPageNum(), pageParam.getPageSize());
        return ResultData.ok(knowledgeList, null);
    }

    @PostMapping("/getAuditPage")
    public ResultData getAuditPage(@RequestBody PageParam pageParam) {
        KnowledgePagerVO knowledgeList = knowledgeService.getAuditPage(pageParam.getPageNum(), pageParam.getPageSize());
        return ResultData.ok(knowledgeList, null);
    }

    @PostMapping("/getPageByType/{id}")
    public ResultData getPageByType(@PathVariable Integer id, @RequestBody PageParam pageParam) {
        KnowledgePagerVO knowledgePagerList = knowledgeService.getPageByType(id, pageParam.getPageNum(), pageParam.getPageSize());
        return ResultData.ok(knowledgePagerList, null);
    }

    @GetMapping("/delete/{id}")
    public ResultData delete(@PathVariable Integer id) {
        knowledgeService.delete(id);
        return ResultData.ok(null);
    }

    @PostMapping("/create")
    public ResultData create(@RequestBody KnowledgeVO knowledgeVO) {
        knowledgeService.create(knowledgeVO);
        return ResultData.ok(null);
    }

    @PostMapping("/getById/{id}")
    public ResultData getById(@PathVariable Integer id) {
        Knowledge knowledge = knowledgeService.getById(id);
        return ResultData.ok(knowledge, null);
    }

    @PostMapping("/modify")
    public ResultData modify(@RequestBody KnowledgeVO knowledgeVO){
        knowledgeService.modify(knowledgeVO);
        return ResultData.ok(null);
    }

    @PostMapping("/fulltext_search")
    public ResultData fulltextSearch(@RequestBody String key) {
        List<Knowledge> knowledges = knowledgeService.fulltextSearch(key);
        return ResultData.ok(knowledges, null);
    }

    @PostMapping("/correct/{id}")
    public ResultData correct(@PathVariable Integer id, @RequestBody String comment) {
        knowledgeService.correct(id, comment);
        return ResultData.ok(null);
    }
}