package finley.gmair.controller;

import finley.gmair.dto.QrCodeParamDTO;
import finley.gmair.vo.ApiResult;
import finley.gmair.service.SummaryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lyy
 * @date 2020-06-30 11:29 下午
 */
@RestController
@RequestMapping("/openplatform/summary")
public class SummaryController {

    @Resource
    private SummaryService summaryService;

    @GetMapping("/pm2_5")
    public ApiResult pm25QueryDaily(String appid, String qrcode) {
        return summaryService.getDailyPM25(appid, qrcode);
    }

    @GetMapping("/power/hourly")
    public ApiResult powerQueryHourly(String appid, String qrcode) {
        return summaryService.getHourly(appid, qrcode, "power");
    }

    @PostMapping("/power/hourly")
    public ApiResult powerQueryListHourly(@RequestBody QrCodeParamDTO qrCodeParamDTO) {
        return summaryService.getHourly(qrCodeParamDTO, "power");
    }

    @GetMapping("/power/daily")
    public ApiResult powerQueryDaily(String appid, String qrcode) {
        return summaryService.getDaily(appid, qrcode, "power");
    }

    @PostMapping("/power/daily")
    public ApiResult powerQueryDaily(@RequestBody QrCodeParamDTO qrCodeParamDTO) {
        return summaryService.getDaily(qrCodeParamDTO, "power");
    }

    @GetMapping("/wind/hourly")
    public ApiResult windQueryHourly(String appid, String qrcode) {
        return summaryService.getHourly(appid, qrcode, "volume");
    }

    @PostMapping("/wind/hourly")
    public ApiResult windQueryHourly(@RequestBody QrCodeParamDTO qrCodeParamDTO) {
        return summaryService.getHourly(qrCodeParamDTO, "volume");
    }

    @GetMapping("/wind/daily")
    public ApiResult windQueryDaily(String appid, String qrcode) {
        return summaryService.getDaily(appid, qrcode, "volume");
    }

    @PostMapping("/wind/daily")
    public ApiResult windQueryDaily(@RequestBody QrCodeParamDTO qrCodeParamDTO) {
        return summaryService.getDaily(qrCodeParamDTO, "volume");
    }

}
