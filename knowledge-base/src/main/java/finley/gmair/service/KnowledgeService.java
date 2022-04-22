package finley.gmair.service;

import finley.gmair.dto.knowledgebase.KnowledgeDTO;
import finley.gmair.model.knowledgebase.Knowledge;
import finley.gmair.vo.knowledgebase.KnowledgePagerVO;
import finley.gmair.vo.knowledgebase.KnowledgeVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KnowledgeService {
    void create(KnowledgeVO knowledgeVO);

    void delete(Integer id);

    void publish(Integer id);

    void reedit(Integer id);

    KnowledgePagerVO getPage(Integer pageNum, Integer pageSize);

    List<KnowledgeVO> getAll();

    KnowledgePagerVO getAuditPage(Integer pageNum, Integer pageSize);

    List<KnowledgeVO> getAudit();

    //KnowledgePagerVO getPageByType(Integer id, Integer pageNum, Integer pageSize);

    KnowledgeVO getById(Integer id);

    void modify(KnowledgeVO knowledgeVO);

    List<Knowledge> fulltextSearch(String key);

    KnowledgePagerVO fulltextListSearch(Integer pageSize, Integer pageNum, List<String> keys);

    List<KnowledgeVO> searchByTagsKeys(List<Integer> tagIdxs, String keywords);

    void correct(KnowledgeDTO knowledgeDTO,int commentId);
}
