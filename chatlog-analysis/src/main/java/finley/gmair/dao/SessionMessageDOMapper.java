package finley.gmair.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface SessionMessageDOMapper {
    void updateSentimentAnalysis(@Param("idAnalysis")Map<Integer,String> map);
}
