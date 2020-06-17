package finley.gmair.dao;

import finley.gmair.model.drift.ReportTemplate;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ReportTemplateDao {

    ResultData query(Map<String,Object> condition);

    ResultData insert(ReportTemplate reportTemplate);
}
