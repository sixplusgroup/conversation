package finley.gmair.enums.knowledgeBase;

/**
 * 状态，0为待解决，1为已解决，2为废弃
 */
public enum CommentStatus {
    UNRESOLVED(0,"待解决"),
    RESOLVED(1,"已解决"),
    ABANDON(2,"废弃");

    private Integer code;
    private String value;

    private CommentStatus(Integer code,String value){
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
        for(CommentStatus commentStatus:CommentStatus.values()){
            if(code.equals(commentStatus.getCode())){
                return commentStatus.getValue();
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
        for(CommentStatus commentStatus:CommentStatus.values()){
            if(value.equals(commentStatus.getValue())){
                return commentStatus.getCode();
            }
        }
        return  null;
    }
}


