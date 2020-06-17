
package finley.gmair.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BackdoorSchedule {
    @Autowired
    private MachineService machineService;

    private final int[][] reference = new int[][]{{80, 200, 300}, {150, 250, 350}, {200, 320, 400}};


    public BackdoorSchedule() {
    }

    @Scheduled(
            fixedRate = 30000L
    )
    public ResultData schedule() {
        ResultData result = new ResultData();

        try {
            String[] qrcode = new String[]{"52A203A929725"};
            (new Thread(() -> {
                for (int i = 0; i < qrcode.length; ++i) {
                    System.out.println("Trigger status obtain for " + qrcode[i]);
                    ResultData response = this.machineService.getMachineStatusByQRcode(qrcode[i]);
                    if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                        System.out.println(JSONObject.toJSONString(response.getData()));
                        JSONObject status = JSON.parseObject(JSONObject.toJSONString(response.getData()));
                        int speed = status.getIntValue("volume");
                        if (status.getIntValue("power") != 0 && status.getIntValue("mode") != 2 && ((status.getIntValue("mode") == 0) || (status.getIntValue("mode") == 1 && exist(reference, speed)))) {
                            int co2 = status.getIntValue("co2");
                            int pm2_5 = status.getIntValue("pm2_5");
                            speed = reference[co2Index(co2)][pm2_5Index(pm2_5)];
                            machineService.configSpeed(qrcode[i], speed);
                        }
                    } else {
                        System.out.println("Fail to obtain machine status for " + qrcode[i]);
                    }
                }

            })).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private int co2Index(int value) {
        if (value <= 1000) {
            return 0;
        }
        if (value <= 1500) {
            return 1;
        }
        return 2;
    }

    private int pm2_5Index(int value) {
        if (value <= 75) {
            return 0;
        }
        if (value <= 150) {
            return 1;
        }
        return 2;
    }

    private boolean exist(int[][] reference, int value) {
        for (int i = 0; i < reference.length; i++) {
            for (int j = 0; j < reference[0].length; j++) {
                if (reference[i][j] == value) {
                    return true;
                }
            }
        }
        return false;
    }
}
