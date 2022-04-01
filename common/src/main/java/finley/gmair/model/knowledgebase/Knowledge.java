package finley.gmair.model.knowledgebase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Knowledge {
    private Integer id;
    private Integer status;
    private Integer views;
    private String title;
    private String content;
    private Date createTime;
    private Date modifyTime;
}
