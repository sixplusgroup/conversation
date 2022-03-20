package finley.gmair.model.knowledgebase;

import lombok.Data;

@Data
public class Knowledge {
    private Integer knowledge_id;
    private Integer status;
    private Integer knowledge_type;
    private String title;
    private String content;
    private Integer views;
}
