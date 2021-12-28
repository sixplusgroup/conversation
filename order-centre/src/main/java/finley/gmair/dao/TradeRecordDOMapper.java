package finley.gmair.dao;

import finley.gmair.model.entity.TradeRecordDO;

public interface TradeRecordDOMapper {
    int insert(TradeRecordDO record);

    int insertSelective(TradeRecordDO record);

    TradeRecordDO selectByPrimaryKey(String recordId);

    int updateByPrimaryKeySelective(TradeRecordDO record);

    int updateByPrimaryKeyWithBLOBs(TradeRecordDO record);

    int updateByPrimaryKey(TradeRecordDO record);
}