package finley.gmair.event;

import finley.gmair.model.domain.UnifiedTrade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 16:40
 * @description ：
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeDefuzzyEvent implements IEvent {
    private UnifiedTrade unifiedTrade;
}
