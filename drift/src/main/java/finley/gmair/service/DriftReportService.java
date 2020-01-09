package finley.gmair.service;

import finley.gmair.model.drift.DataItem;
import finley.gmair.model.drift.DriftReport;
import finley.gmair.model.drift.ReportTemplate;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface DriftReportService {

    ResultData fetchDriftReport(Map<String,Object> condition);

    ResultData createDriftReport(DriftReport driftReport);

    ResultData fetchReportTemplate(Map<String,Object> condition);

    ResultData createReportTemplate(ReportTemplate reportTemplate);

    ResultData fetchDataItem(Map<String,Object> condition);

    ResultData createDataItem(DataItem dataItem);
}
