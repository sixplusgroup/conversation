package finley.gmair.controller;

import finley.gmair.form.machine.BoardVersionForm;
import finley.gmair.model.machine.v2.BoardVersion;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/machine/board")
public class BoardVersionController {

    @PostMapping("/record/single")
    public ResultData recordSingleBoardVersion(BoardVersionForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getMachineId()) || StringUtils.isEmpty(form.getVersion())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all required information");
            return result;
        }
        BoardVersion version = new BoardVersion(form.getMachineId(), form.getVersion());

        return result;
    }
}
