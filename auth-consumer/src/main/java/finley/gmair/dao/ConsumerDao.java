package finley.gmair.dao;

import finley.gmair.form.consumer.ConsumerPartInfoQuery;
import finley.gmair.model.consumer.Consumer;
import finley.gmair.util.ResultData;
import finley.gmair.vo.consumer.ConsumerPartInfoVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface ConsumerDao {
    @Transactional
    ResultData insert(Consumer consumer);

    ResultData query(Map<String, Object> condition);

    List<ConsumerPartInfoVo> queryConsumerAccounts(ConsumerPartInfoQuery query);

    long queryConsumerAccountsSize(ConsumerPartInfoQuery query);

    ResultData update(Map<String, Object> condition);
}
