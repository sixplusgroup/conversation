package finley.gmair.dao;

import finley.gmair.model.assemble.Barcode;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface BarcodeDao {
    ResultData insert(Barcode barcode);

    ResultData insertBatch(List<Barcode> barcodeList);

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
