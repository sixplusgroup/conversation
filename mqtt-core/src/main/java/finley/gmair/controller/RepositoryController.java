package finley.gmair.controller;

import finley.gmair.util.ResultData;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: RepositoryController
 * @Description: TODO
 * @Author fan
 * @Date 2019/6/13 11:30 AM
 */
@RestController
public class RepositoryController {
    
    public ResultData isOnline() {
        ResultData result = new ResultData();

        return result;
    }
}
