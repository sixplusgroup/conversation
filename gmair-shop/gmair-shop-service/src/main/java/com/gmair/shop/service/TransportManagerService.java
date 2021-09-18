

package com.gmair.shop.service;

import com.gmair.shop.bean.app.dto.ProductItemDto;
import com.gmair.shop.bean.model.UserAddr;

public interface TransportManagerService {

	Double calculateTransfee(ProductItemDto productItem, UserAddr userAddr);
}
