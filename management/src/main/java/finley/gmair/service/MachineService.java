package finley.gmair.service;

import finley.gmair.form.machine.ControlOptionForm;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("machine-agent")
public interface MachineService {

    @GetMapping("/machine/goods/model/list")
    ResultData modelList();

    @GetMapping("/machine/qrcode/batch/list")
    ResultData batchList(@RequestParam("modelId") String modelId);

    @PostMapping("/machine/qrcode/check")
    ResultData check(@RequestParam("candidate") String candidate);

    @PostMapping("/machine/control/option/create")
    ResultData setControlOption(@RequestParam("optionName") String optionName,
                                @RequestParam("optionComponent") String optionComponent,
                                @RequestParam("modelId") String modelId,
                                @RequestParam("actionName") String actionName,
                                @RequestParam("actionOperator") String actionOperator,
                                @RequestParam("commandValue") String commandValue);

    @GetMapping("/machine/qrcode/checkonline")
    ResultData checkOnline(@RequestParam("qrcode") String qrcode);

    @GetMapping("/machine/qrcode/model")
    ResultData getModel(@RequestParam("codeValue") String codeValue);

    @GetMapping("/machine/status/byuid")
    ResultData machineStatus(@RequestParam("uid") String uid);

    @GetMapping("machine/map/fetch")
    ResultData fetchMachineLatLngList();

    @GetMapping("/machine/latest/pm2_5/24hour/probe/bycode")
    ResultData probeLast24HourOutPm25ByQrcode(@RequestParam("qrcode") String qrcode);

    @GetMapping("/machine/status/hourly")
    ResultData fetchMachineHourlyPm2_5(@RequestParam("qrcode") String qrcode);

    @GetMapping("/machine/consumer/owner/machine/list")
    ResultData getOwnerMachineList(@RequestParam("curPage") int curPage,
                                   @RequestParam("pageSize") int pageSize,
                                   @RequestParam("qrcode") String qrcode,
                                   @RequestParam("phone") String phone,
                                   @RequestParam("createTimeGTE") String createTimeGTE,
                                   @RequestParam("createTimeLTE") String createTimeLTE,
                                   @RequestParam("online") String online,
                                   @RequestParam("overCount") String overCount,
                                   @RequestParam("overCountGTE") String overCountGTE,
                                   @RequestParam("overCountLTE") String overCountLTE);

    @GetMapping("/machine/latest/pm2_5/lastNday")
    ResultData fetchLastNDayData(@RequestParam("qrcode") String qrcode, @RequestParam("lastNday") int lastNday);

    @GetMapping("/machine/latest/pm2_5/lastNhour")
    ResultData fetchLastNHourData(@RequestParam("qrcode") String qrcode, @RequestParam("lastNhour") int lastNhour);

    @GetMapping("/machine/info/list/daily/cityaqi/lastNhour")
    ResultData getCitylastNhourData(@RequestParam("qrcode") String qrcode, @RequestParam("lastNhour") int lastNhour);

    @GetMapping("/machine/info/list/daily/cityaqi/lastNday")
    ResultData getCitylastNdayData(@RequestParam("qrcode") String qrcode, @RequestParam("lastNday") int lastNday);
}
