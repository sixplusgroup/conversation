package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ReportTemplateDao;
import finley.gmair.model.drift.DriftReport;
import finley.gmair.model.drift.ReportTemplate;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ReportTemplateDaoImpl extends BaseDao implements ReportTemplateDao {
    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<DriftReport> list = sqlSession.selectList("gmair.drift.reporttemplate.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insert(ReportTemplate reportTemplate) {
        ResultData result = new ResultData();
        reportTemplate.setReportTemplateId(IDGenerator.generate("DRT"));
        try {
            sqlSession.insert("gmair.drift.reporttemplate.insert", reportTemplate);
            result.setData(reportTemplate);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
