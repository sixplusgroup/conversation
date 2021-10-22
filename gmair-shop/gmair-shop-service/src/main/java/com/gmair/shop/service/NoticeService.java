

package com.gmair.shop.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.app.dto.NoticeDto;
import com.gmair.shop.bean.model.Notice;

import java.util.List;

/**
 * 公告管理
 *
 *
 * @date 2019-04-18 21:21:40
 */
public interface NoticeService extends IService<Notice> {

    List<Notice> listNotice();

    void removeNoticeList();

    Page<NoticeDto> pageNotice(Page<NoticeDto> page);

    Notice getNoticeById(Long noticeId);

    void removeNoticeById(Long noticeId);
}
