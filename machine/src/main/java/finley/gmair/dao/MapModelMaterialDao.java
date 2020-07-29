package finley.gmair.dao;

import finley.gmair.util.ResultData;
import org.apache.ibatis.annotations.Param;

public interface MapModelMaterialDao {

    ResultData selectMaterialLinkByModelId(@Param("modelId") String modelId);
}