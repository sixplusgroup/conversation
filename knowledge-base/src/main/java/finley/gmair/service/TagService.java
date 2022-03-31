package finley.gmair.service;

import finley.gmair.model.knowledgebase.Knowledge;
import finley.gmair.model.knowledgebase.Tag;
import finley.gmair.model.knowledgebase.Type;
import finley.gmair.vo.knowledgebase.KnowledgePagerVO;
import finley.gmair.vo.knowledgebase.KnowledgeVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    void create(String tagName);
    void delete(Integer tagId);
    void modify(Tag tag);
    void modifyKnowledgeTag(Integer knowledgeId, List<Integer> addTagIds, List<Integer> deleteTagIds);
    List<Tag> getAllTags();
    List<Tag> getTagsByKnowledge(Integer id);

    KnowledgePagerVO getPageByTags(List<Integer> tagIds, Integer pageNum, Integer pageSize);
}
