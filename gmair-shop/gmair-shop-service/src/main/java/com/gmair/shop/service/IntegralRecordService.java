package com.gmair.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.model.IntegralRecord;

import java.util.List;

/**
 * @Author Joby
 */
public interface IntegralRecordService extends IService<IntegralRecord> {

    void createRecord(IntegralRecord integralRecord);

    List<IntegralRecord> getMyRecordsByUserId(String userId);
}
