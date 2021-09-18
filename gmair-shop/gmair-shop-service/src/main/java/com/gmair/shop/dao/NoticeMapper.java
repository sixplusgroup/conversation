

package com.gmair.shop.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gmair.shop.bean.app.dto.NoticeDto;
import com.gmair.shop.bean.model.Notice;

/**
 * 公告管理
 *
 *
 * @date 2019-04-18 21:21:40
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    Page<NoticeDto> pageNotice(Page<NoticeDto> page);
}
