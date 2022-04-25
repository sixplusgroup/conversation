package finley.gmair.controller;

import finley.gmair.service.KnowledgeService;
import finley.gmair.util.ResultData;
import finley.gmair.utils.PageParam;
import finley.gmair.utils.SearchPageParam;
import finley.gmair.utils.SearchParam;
import finley.gmair.vo.knowledgebase.KnowledgePagerVO;
import finley.gmair.vo.knowledgebase.KnowledgeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/knowledge-base/knowledge")
public class KnowledgeController {

    @Autowired
    KnowledgeService knowledgeService;

    /**
     * 审核者审核通过，发布知识
     * @param id
     * @return
     *
     */
    @PreAuthorize("hasAuthority('knowledge_audit')")
    @PostMapping("/publish")
    public ResultData publish(@RequestParam Integer id) {
        knowledgeService.publish(id);
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
     * 获取所有知识
     * @param
     * @return
     *
     */
    @PostMapping("/getAll")
    public ResultData getAll() {

        List<KnowledgeVO> knowledgeList = knowledgeService.getAll();
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
     * 获取"待审核"状态的知识
     * @param
     * @return
     *
     */
    @PostMapping("/getAudit")
    public ResultData getAudit() {
        List<KnowledgeVO> knowledgeList = knowledgeService.getAudit();
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
    @PreAuthorize("hasAuthority('knowledge_update')")
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
    @PreAuthorize("hasAuthority('knowledge_update')")
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
     * 修改某条知识(除了modifyTime都传， 其中知识ID, title, content必传)
     * @param knowledgeVO
     * @return
     *
     */
    @PreAuthorize("hasAuthority('knowledge_update')")
    @PostMapping("/modify")
    public ResultData modify(@RequestBody KnowledgeVO knowledgeVO){
        knowledgeService.modify(knowledgeVO);
        return ResultData.ok(null);
    }

    /**
     * 根据搜索关键字（用空格分割），对标题和内容进行全文搜索
     * @param searchPageParam
     * @return
     *
     */
    @PostMapping("/fulltext_search")
    public ResultData fulltextSearch(@RequestBody SearchPageParam searchPageParam) { //todo test
        //split , search each key, and then order list by views
        KnowledgePagerVO knowledgeList = knowledgeService.fulltextListSearch(searchPageParam.getPageSize(), searchPageParam.getPageNum(),Arrays.asList(searchPageParam.getKey().split(" ")));
        return ResultData.ok(knowledgeList, null);
    }

    /**
     * 根据keys tags获取知识
     * @param searchParam
     * @return
     */
    @PostMapping("/searchByTagsKeys")
    public ResultData searchByTagsKeys(@RequestBody SearchParam searchParam) { //todo test
        //split , search each key, and then order list by views
        List<KnowledgeVO> knowledgeList = knowledgeService.searchByTagsKeys(searchParam.getTags(), searchParam.getKeywords());
        return ResultData.ok(knowledgeList, null);
    }

    @GetMapping("/keywords")
    public ResultData getKeywords(){
        String keywords = knowledgeService.getKeywords();
        return ResultData.ok(keywords);
    }

}