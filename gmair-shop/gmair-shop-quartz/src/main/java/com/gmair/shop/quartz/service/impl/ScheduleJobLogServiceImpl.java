

package com.gmair.shop.quartz.service.impl;

import com.gmair.shop.quartz.dao.ScheduleJobLogMapper;
import com.gmair.shop.quartz.model.ScheduleJobLog;
import com.gmair.shop.quartz.service.ScheduleJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 *
 */
@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogMapper, ScheduleJobLog> implements ScheduleJobLogService {

	@Autowired
	private ScheduleJobLogMapper scheduleJobLogMapper;

}
