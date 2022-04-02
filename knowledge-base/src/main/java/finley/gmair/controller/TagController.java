package finley.gmair.controller;

import finley.gmair.model.knowledgebase.Tag;
import finley.gmair.model.knowledgebase.TagRelation;
import finley.gmair.model.knowledgebase.Type;
import finley.gmair.service.TagService;
import finley.gmair.util.ResultData;
import finley.gmair.utils.PageParam;
import finley.gmair.utils.TagsPageParam;
import finley.gmair.vo.knowledgebase.KnowledgePagerVO;
import finley.gmair.vo.knowledgebase.KnowledgeTagsVO;
import finley.gmair.vo.knowledgebase.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
public class TagController {
    @Autowired
    TagService tagService;
    @PostMapping("/create")
    public ResultData create(@RequestBody TagVO tagVO) {
//        try{
//            tagService.create(tagVO.getTag_name());
//            return ResultData.ok(null);
//        }catch (Exception e) {
//            return ResultData.error("标签名已存在！");
//        }
        tagService.create(tagVO.getTag_name());
        return ResultData.ok(null);
    }

    @PostMapping("/delete/{id}")
    public ResultData delete(@PathVariable Integer id) {
        tagService.delete(id);
        return ResultData.ok(null);
    }

    @PostMapping("/modifyTagName")
    public ResultData modify(@RequestBody Tag tag) {
        tagService.modify(tag);
        return ResultData.ok(null);
    }

    @PostMapping("/modifyKnowledgeTags")
    public ResultData knowledgeTags(@RequestBody KnowledgeTagsVO knowledgeTagsVO) {
        tagService.modifyKnowledgeTag(knowledgeTagsVO.getKnowledge_id(), knowledgeTagsVO.getAdd_tags(), knowledgeTagsVO.getDelete_tags());
        return ResultData.ok(null);
    }

    @PostMapping("/getAllTags")
    public ResultData getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResultData.ok(tags, null);
    }

    @PostMapping("/getTagsByKnowledge/{id}")
    public ResultData getTagsByKnowledge(@PathVariable Integer id) {
        List<Tag> tags = tagService.getTagsByKnowledge(id);
        return ResultData.ok(tags, null);
    }

    @PostMapping("/getByTags")
    public ResultData getPageByTags(@RequestBody TagsPageParam pageParam) {
        KnowledgePagerVO knowledgePagerList = tagService.getPageByTags(pageParam.getTag_ids(), pageParam.getPageNum(), pageParam.getPageSize());
        return ResultData.ok(knowledgePagerList, null);
    }

}
