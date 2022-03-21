package finley.gmair.controller;

import finley.gmair.model.knowledgebase.Knowledge;
import finley.gmair.service.KnowledgeService;
import finley.gmair.util.ResultData;
import finley.gmair.utils.PageParam;
import finley.gmair.vo.knowledgebase.KnowledgePagerVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledgebase")
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

    @GetMapping("/reedit/{id}")
    public ResultData reedit(@PathVariable Integer id) {
        knowledgeService.reedit(id);
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
    public ResultData create(@RequestBody Knowledge knowledge) {
        knowledgeService.create(knowledge);
        return ResultData.ok(null);
    }

    @PostMapping("/getById/{id}")
    public ResultData getById(@PathVariable Integer id) {
        knowledgeService.getById(id);
        return ResultData.ok(null);
    }

    @PostMapping("/modify")
    public ResultData modify(@RequestBody Knowledge knowledge){
        knowledgeService.modify(knowledge);
        return ResultData.ok(null);
    }
}