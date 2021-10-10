

package com.gmair.shop.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmair.shop.sys.dao.SysLogMapper;
import com.gmair.shop.sys.model.SysLog;
import com.gmair.shop.sys.service.SysLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service("sysLogService")
@AllArgsConstructor
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

	private final SysLogMapper sysLogMapper;
}
