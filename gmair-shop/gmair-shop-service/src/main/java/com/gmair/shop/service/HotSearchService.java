

package com.gmair.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.dto.HotSearchDto;
import com.gmair.shop.bean.model.HotSearch;
import com.gmair.shop.dao.HotSearchMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *
 *
 */
public interface HotSearchService extends IService<HotSearch> {

    List<HotSearchDto> getHotSearchDtoByshopId(Long shopId);

    void removeHotSearchDtoCacheByshopId(Long shopId);
}
