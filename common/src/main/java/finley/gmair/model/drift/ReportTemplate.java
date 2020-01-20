package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class ReportTemplate extends Entity {

    private String reportTemplateId;

    private String detectName;

    private String evaluateBasis;

    private String knowledge;

    public ReportTemplate() {
        super();
    }

    public ReportTemplate(String detectName, String evaluateBasis, String knowledge) {
        this();
        this.detectName = detectName;
        this.evaluateBasis = evaluateBasis;
        this.knowledge = knowledge;
    }

    public String getReportTemplateId() {
        return reportTemplateId;
    }

    public void setReportTemplateId(String reportTemplateId) {
        this.reportTemplateId = reportTemplateId;
    }

    public String getDetectName() {
        return detectName;
    }

    public void setDetectName(String detectName) {
        this.detectName = detectName;
    }

    public String getEvaluateBasis() {
        return evaluateBasis;
    }

    public void setEvaluateBasis(String evaluateBasis) {
        this.evaluateBasis = evaluateBasis;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }
}
