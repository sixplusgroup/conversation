package finley.gmair.model.dto;

import com.taobao.api.domain.Trade;
import finley.gmair.util.IDGenerator;
import lombok.Data;

/**
 * @author zm
 * @date 2020/10/21 0021 12:33
 * @description 淘宝Trade的包装类，用于转存到中台系统trade表中
 **/
@Data
public class TbTradeDTO {
    private Trade tbTrade;
    private String tradeId;

    public TbTradeDTO() {
    }

    public TbTradeDTO(Trade tbTrade) {
        this.tbTrade = tbTrade;
        this.tradeId = IDGenerator.generate("CentreTrade");
    }
}
