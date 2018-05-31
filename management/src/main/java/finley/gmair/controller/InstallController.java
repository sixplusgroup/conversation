package finley.gmair.controller;

import finley.gmair.service.InstallService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management/install")
public class InstallController {

    @Autowired
    private InstallService installService;

    @GetMapping("/team/list")
    public ResultData teamList() {
        return installService.fetchTeamList();
    }
}
