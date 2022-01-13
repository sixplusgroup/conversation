package finley.gmair.repo;

import finley.gmair.converter.TradeRecordDataConverter;
import finley.gmair.dao.TradeRecordDOMapper;
import finley.gmair.model.domain.TradeRecord;
import finley.gmair.model.entity.TradeRecordDO;
import finley.gmair.util.IDGenerator;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-23 15:54
 * @description ：
 */

@Repository
public class TradeRecordRepo {
    @Resource
    TradeRecordDOMapper tradeRecordDOMapper;

    @Resource
    TradeRecordDataConverter tradeRecordDataConverter;

    public void add(TradeRecord tradeRecord) {
        tradeRecord.setRecordId(IDGenerator.generate("REC"));
        TradeRecordDO tradeRecordDO = tradeRecordDataConverter.toData(tradeRecord);
        Date now = new Date();
        tradeRecordDO.setSysCreateTime(now);
        tradeRecordDO.setSysUpdateTime(now);
        tradeRecordDOMapper.insertSelective(tradeRecordDO);
    }
}
