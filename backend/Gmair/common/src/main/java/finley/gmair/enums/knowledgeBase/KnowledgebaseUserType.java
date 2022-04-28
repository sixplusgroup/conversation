package finley.gmair.enums.knowledgeBase;

public enum KnowledgebaseUserType {
    MANAGER(1,"Manager"),
    EDITOR(2,"Market"),
    USER(3,"Client");

    private Integer code;
    private String value;

    private KnowledgebaseUserType(Integer code,String value){
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
        for(KnowledgebaseUserType knowledgebaseUserType:KnowledgebaseUserType.values()){
            if(code.equals(knowledgebaseUserType.getCode())){
                return knowledgebaseUserType.getValue();
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
        for(KnowledgebaseUserType knowledgebaseUserType:KnowledgebaseUserType.values()){
            if(value.equals(knowledgebaseUserType.getValue())){
                return knowledgebaseUserType.getCode();
            }
        }
        return  null;
    }
}
