package finley.gmair.enums.knowledgeBase;


public enum CommentType {
    AUDITORS(0,"审核人员反馈"),
    USERS(1,"使用人员反馈");

    private Integer code;
    private String value;

    private CommentType(Integer code,String value){
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
        for(CommentType commentType:CommentType.values()){
            if(code.equals(commentType.getCode())){
                return commentType.getValue();
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
        for(CommentType commentType:CommentType.values()){
            if(value.equals(commentType.getValue())){
                return commentType.getCode();
            }
        }
        return  null;
    }
}
