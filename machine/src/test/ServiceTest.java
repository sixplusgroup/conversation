import finley.gmair.machine.MachineApplication;
import finley.gmair.model.machine.MachineQrcodeBind;
import finley.gmair.service.MachineQrcodeBindService;
import finley.gmair.util.ResultData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MachineApplication.class)
public class ServiceTest {
    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    @Autowired
    private LatestPM2_5Service latestPM2_5Service;

    @Test
    public void insert() {
        LatestPM2_5 latestPM2_5 = new LatestPM2_5();
        latestPM2_5.setMachineId("123456789");
        latestPM2_5.setPm2_5("123");
        ResultData response = latestPM2_5Service.insert(latestPM2_5);
        System.out.println(response);
    }

    @Test
    public void fetch() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("qrcode", "35A108A123432");
        ResultData response = machineQrcodeBindService.fetch(condition);
        System.out.println(response);
    }
}
