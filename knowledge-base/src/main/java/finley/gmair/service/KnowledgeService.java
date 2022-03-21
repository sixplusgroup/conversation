package finley.gmair.service;

import finley.gmair.model.knowledgebase.Knowledge;
import finley.gmair.util.ResultData;
import finley.gmair.utils.PageParam;
import finley.gmair.vo.knowledgebase.KnowledgePagerVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public interface KnowledgeService {
    void insert(Knowledge knowledge);

    void delete(Integer id);

    void publish(Integer id);

    void reedit(Integer id);

    KnowledgePagerVO getPage(Integer pageNum, Integer pageSize);

    KnowledgePagerVO getAuditPage(Integer pageNum, Integer pageSize);
}
