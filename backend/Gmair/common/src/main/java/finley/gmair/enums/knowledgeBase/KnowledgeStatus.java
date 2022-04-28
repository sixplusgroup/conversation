package finley.gmair.enums.knowledgeBase;

/**
 * 状态，0待审核，1待编辑，2发布
 */
public enum KnowledgeStatus {
    PENDING_REVIEW(0,"待审核"),
    PENDING_DDIT(1,"待编辑"),
    PENDING_PUBLISH(2,"发布");

    private Integer code;
    private String value;

    private KnowledgeStatus(Integer code,String value){
        this.code=code;
        this.value=value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 根据code获取去value
     * @param code
     * @return
     */
    public static String getValueByCode(Integer code){
        for(KnowledgeStatus knowledgeStatus:KnowledgeStatus.values()){
            if(code.equals(knowledgeStatus.getCode())){
                return knowledgeStatus.getValue();
            }
        }
        return  null;
    }

    /**
     * 根据value获得code
     * @param value
     * @return
     */
    public static Integer getCodeByValue(String value){
        for(KnowledgeStatus knowledgeStatus:KnowledgeStatus.values()){
            if(value.equals(knowledgeStatus.getValue())){
                return knowledgeStatus.getCode();
            }
        }
        return  null;
    }
}
