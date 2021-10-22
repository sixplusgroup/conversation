

package com.gmair.shop.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.enums.SmsType;
import com.gmair.shop.bean.model.SmsLog;

/**
 *
 *
 */
public interface SmsLogService extends IService<SmsLog> {

	public void sendSms(SmsType smsType,String userId,String mobile,Map<String,String> params);
	
	public boolean checkValidCode(String mobile, String code,SmsType smsType);
}
