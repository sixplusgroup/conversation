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

    /**
     * 新建标签，包含对重复的检查
     * @param tagVO
     * @return
     */
    @PostMapping("/create")
    public ResultData create(@RequestBody TagVO tagVO) {
        try{
            tagService.create(tagVO.getTag_name());
            return ResultData.ok(null);
        }catch (Exception e) {
            return ResultData.error("标签名已存在！");
        }
    }

    /**
     * 删除某个tag
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public ResultData delete(@PathVariable Integer id) {
        tagService.delete(id);
        return ResultData.ok(null);
    }

    /**
     * 修改某个标签名🏷
     * @param tag
     * @return
     */
    @PostMapping("/modifyTagName")
    public ResultData modify(@RequestBody Tag tag) {
        try{
            tagService.modify(tag);
            return ResultData.ok(null);
        }catch (Exception e) {
            return ResultData.error("标签名已存在！");
        }

    }

    /**
     * 新建或者修改knowledge的标签
     * 传入要增加的tagID列表和要删除的tagID列表，「没有对重复添加做检查！删除不存在的标签可以。没有对knowledge是否存在做检查」
     * @param knowledgeTagsVO
     * @return
     */
    @PostMapping("/modifyKnowledgeTags")
    public ResultData knowledgeTags(@RequestBody KnowledgeTagsVO knowledgeTagsVO) {
        tagService.modifyKnowledgeTag(knowledgeTagsVO.getKnowledge_id(), knowledgeTagsVO.getAdd_tags(), knowledgeTagsVO.getDelete_tags());
        return ResultData.ok(null);
    }

    /**
     * 获得所有的标签
     * @return
     */
    @PostMapping("/getAllTags")
    public ResultData getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResultData.ok(tags, null);
    }

    /**
     * 获取某条知识的所有标签
     * @param id
     * @return
     */
    @PostMapping("/getTagsByKnowledge/{id}")
    public ResultData getTagsByKnowledge(@PathVariable Integer id) {
        List<Tag> tags = tagService.getTagsByKnowledge(id);
        return ResultData.ok(tags, null);
    }

    /**
     * 根据tag的列表获取知识
     * @param pageParam
     * @return
     */
    @PostMapping("/getByTags")
    public ResultData getPageByTags(@RequestBody TagsPageParam pageParam) {
        KnowledgePagerVO knowledgePagerList = tagService.getPageByTags(pageParam.getTag_ids(), pageParam.getPageNum(), pageParam.getPageSize());
        return ResultData.ok(knowledgePagerList, null);
    }

}
