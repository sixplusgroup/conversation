package finley.gmair.model.installation;

import finley.gmair.model.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AssignReport extends Entity {
    private String assignId;

    private String codeValue;

    private String assignDetail;

    private AssignStatus assignStatus;

    private Date assignDate;

    private String assignSource;

    private String consumerConsignee;

    private String consumerPhone;

    private String consumerAddress;

    private String teamId;

    private String teamName;

    private String memberId;

    private String memberName;
}
