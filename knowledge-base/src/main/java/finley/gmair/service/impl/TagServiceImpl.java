package finley.gmair.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import finley.gmair.converter.KnowledgeConverter;
import finley.gmair.dao.KnowledgeMapper;
import finley.gmair.dao.TagMapper;
import finley.gmair.dao.TagRelationMapper;
import finley.gmair.dao.TypeMapper;
import finley.gmair.dto.knowledgebase.CommentDTO;
import finley.gmair.model.knowledgebase.Knowledge;
import finley.gmair.model.knowledgebase.Tag;
import finley.gmair.model.knowledgebase.TagRelation;
import finley.gmair.model.knowledgebase.Type;
import finley.gmair.service.TagService;
import finley.gmair.service.TypeService;
import finley.gmair.util.TimeUtil;
import finley.gmair.vo.knowledgebase.KnowledgePagerVO;
import finley.gmair.vo.knowledgebase.KnowledgeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagMapper tagMapper;
    @Autowired
    TagRelationMapper tagRelationMapper;
    @Autowired
    KnowledgeMapper knowledgeMapper;

    @Override
    public void create(String tag_name) {
//        Tag tag = tagMapper.getByName(tagName); //在tag表中对tag_name建立索引
//        if (tag==null)
//            tagMapper.insert(tagName);
//        else{
//            throw new IllegalArgumentException();
//        }
        tagMapper.insert(tag_name);

    }

    @Override
    public void delete(Integer tagId) {
        tagMapper.delete(tagId);
    }

    @Override
    public void modify(Tag tag) {
        tagMapper.modify(tag);
    }

    @Override
    public void modifyKnowledgeTag(Integer knowledgeId, List<Integer> addTagIds, List<Integer> deleteTagIds) {
        //添加
        for(Integer tagId: addTagIds) {
            TagRelation tagReltion = new TagRelation();
            tagReltion.setTag_id(tagId);
            tagReltion.setKnowledge_id(knowledgeId);
            tagRelationMapper.insert(tagReltion);
        }
        //删除
        for(Integer tagId: deleteTagIds) {
            TagRelation tagReltion = new TagRelation();
            tagReltion.setTag_id(tagId);
            tagReltion.setKnowledge_id(knowledgeId);
            tagRelationMapper.delete(tagReltion);
        }
    }

    @Override
    public List<Tag> getAllTags() {
        return tagMapper.getAll();
    }

    @Override
    public List<Tag> getTagsByKnowledge(Integer knowledge_id) {
        return tagMapper.getTagsByKnowledge(knowledge_id);
    }

    @Override
    public KnowledgePagerVO getPageByTags(List<Integer> tagIds, Integer pageNum, Integer pageSize) {
        List<Integer> knowledgeIds = tagRelationMapper.getByKnowledgeId(tagIds.get(0)).stream().map(TagRelation::getKnowledge_id).collect(Collectors.toList());
        if(tagIds.size()>1){
            for (int i=1;i<tagIds.size();i++){//在tag_relation表中对tag_id建立索引
                //https://www.cnblogs.com/Andya/p/14037640.html
                List<Integer> tmp_knowledgeIds = tagRelationMapper.getByKnowledgeId(tagIds.get(0)).stream().map(TagRelation::getKnowledge_id).collect(Collectors.toList());
                knowledgeIds=knowledgeIds.stream().filter(tmp_knowledgeIds::contains).collect(Collectors.toList());
            }
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Knowledge> knowledges = knowledgeMapper.getByIdList(knowledgeIds);

        KnowledgePagerVO knowledgePagerVO = new KnowledgePagerVO();
        List<KnowledgeVO> knowledgeVOs = knowledges.stream().map(KnowledgeConverter::model2VO).collect(Collectors.toList());
        knowledgePagerVO.setKnowledgeVOS(knowledgeVOs);
        PageInfo<Knowledge> pageInfo = new PageInfo<>(knowledges);
        Long totalNum = pageInfo.getTotal();
        knowledgePagerVO.setTotalNum(totalNum);
        return knowledgePagerVO;
    }


}
