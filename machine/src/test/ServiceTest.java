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

    @Test
    public void insert() {
        MachineQrcodeBind machineQrcodeBind = new MachineQrcodeBind();
        machineQrcodeBind.setBindId("123456789");
        machineQrcodeBind.setCodeValue("35A108A123432");
        machineQrcodeBind.setMachineId("123123");
        ResultData response = machineQrcodeBindService.insert(machineQrcodeBind);
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
