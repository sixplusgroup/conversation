package finley.gmair.service;

import feign.codec.Encoder;
import finley.gmair.form.machine.MachineFilterInfoQuery;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.FilterUpdByFormulaConfig;
import finley.gmair.vo.machine.FilterUpdByMQTTConfig;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "machine-agent", configuration = MachineService.RequestBodySupportConfig.class)
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

    @GetMapping("/machine/qrcode/check/existqrcode")
    ResultData checkQRcodeExist(@RequestParam("codeValue") String codeValue);

    @GetMapping("/machine/model/enabled/component/fetch")
    ResultData fetchModelEnabledComponent(@RequestParam("modelId") String modelId,
                                          @RequestParam("componentName") String componentName);

    @PostMapping("/machine/control/option/operate")
    ResultData chooseComponent(@RequestParam("qrcode") String qrcode,
                               @RequestParam("component") String component,
                               @RequestParam("operation") String operation);

    @PostMapping("/machine/control/option/config/screen")
    ResultData configScreen(@RequestParam("qrcode") String qrcode, @RequestParam("screen") int screen);

    @GetMapping("/machine/consumer/qrcode/bind/list")
    ResultData qrcodeBindList(@RequestParam("search") String search);

    @PostMapping("/machine/consumer/qrcode/unbind")
    ResultData unbindConsumerWithQRcode(@RequestParam("consumerId") String consumerId,
                                        @RequestParam("qrcode") String qrcode);

    @GetMapping("/machine/qrcode/findbyqrcode/consumer")
    ResultData qrcodeGetMachineId(@RequestParam("codeValue") String codeValue);

    @PostMapping("/machine/filter/info/query")
    ResultData queryMachineFilterInfo(@RequestBody MachineFilterInfoQuery query);

    @GetMapping("/machine/filter/info/model/name")
    ResultData queryMachineModelName();

    @GetMapping("/machine/filter/info/model/code")
    ResultData queryMachineModelCode(@RequestParam("modelName") String modelName);

    @GetMapping("/machine/filter/info/config/updatedByMQTT")
    ResultData queryConfigUpdatedByMQTT();

    @PostMapping("/machine/filter/info/update/config/updatedByMQTT")
    ResultData updateConfigUpdatedByMQTT(@RequestBody FilterUpdByMQTTConfig config);

    @GetMapping("/machine/filter/info/config/updatedByFormula")
    ResultData queryConfigUpdatedByFormula();

    @PostMapping("/machine/filter/info/update/config/updatedByFormula")
    ResultData updateConfigUpdatedByFormula(@RequestBody FilterUpdByFormulaConfig config);

    /**
     * 当方法参数中有 @RequestBody 注解的时候，需要使用SpringEncoder而不是SpringFormEncoder，
     * 或者是SpringFormEncoder(new SpringEncoder)，由于在OrderNewService中初始化了SpringFormEncoder，
     * 所以在这里需要另外初始化一个SpringEncoder。
     */
    @Configuration
    class RequestBodySupportConfig {
        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;

        @Bean
        public Encoder feignEncoder() {
            return new SpringEncoder(this.messageConverters);
        }
    }
}
