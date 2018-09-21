package finley.gmair.service;

import finley.gmair.model.assemble.Barcode;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface BarcodeService {
    ResultData create(Barcode barcode);

    ResultData createBatch(List<Barcode> barcodeList);

    ResultData fetch(Map<String, Object> condition);
}
