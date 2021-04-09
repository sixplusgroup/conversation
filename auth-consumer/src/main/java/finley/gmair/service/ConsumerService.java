package finley.gmair.service;

import finley.gmair.form.consumer.ConsumerPartInfoQuery;
import finley.gmair.model.consumer.Address;
import finley.gmair.model.consumer.Consumer;
import finley.gmair.util.ResultData;
import finley.gmair.vo.consumer.ConsumerPartInfoVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface ConsumerService {
    @Transactional
    ResultData createConsumer(Consumer consumer);

    ResultData fetchConsumer(Map<String, Object> condition);

    /**
     * 返回的单个对象与 this.fetchConsumer 的返回值不是同一个VO
     * 所以单独写一个方法
     * @param query 查询条件对象
     * @return 查询结果
     */
    List<ConsumerPartInfoVo> fetchConsumerAccounts(ConsumerPartInfoQuery query);

    /**
     * 根据query给出的条件查询符合条件的账户总数
     * @param query 查询条件对象
     * @return 查询结果
     */
    long fetchConsumerAccountsSize(ConsumerPartInfoQuery query);

    ResultData modifyConsumer(Map<String, Object> condition);

    ResultData fetchConsumerAddress(Map<String, Object> condition);

    ResultData modifyConsumerAddress(Map<String, Object> condition);

    ResultData createConsumerAddress(Address address, String consumerId);

    ResultData fetchConsumerPhone(Map<String, Object> condition);

    ResultData modifyConsumerPhone(Map<String, Object> condition);

    boolean exist(Map<String, Object> condition);
}
