package finley.gmair.service;

import finley.gmair.dto.knowledgebase.CommentDTO;
import finley.gmair.model.knowledgebase.Knowledge;
import finley.gmair.util.ResultData;
import finley.gmair.utils.PageParam;
import finley.gmair.vo.knowledgebase.KnowledgePagerVO;
import finley.gmair.vo.knowledgebase.KnowledgeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public interface KnowledgeService {
    void create(KnowledgeVO knowledgeVO);

    void delete(Integer id);

    void publish(Integer id);

    void reedit(Integer id, CommentDTO comment);

    KnowledgePagerVO getPage(Integer pageNum, Integer pageSize);

    KnowledgePagerVO getAuditPage(Integer pageNum, Integer pageSize);

    //KnowledgePagerVO getPageByType(Integer id, Integer pageNum, Integer pageSize);

    Knowledge getById(Integer id);

    void modify(KnowledgeVO knowledgeVO);

    List<Knowledge> fulltextSearch(String key);

    void correct(Integer id, String comment);
}
