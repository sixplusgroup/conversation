package finley.gmair.controller;


import finley.gmair.service.AirQualityStatisticService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/airquality/schedule")
public class ScheduleController {

    @Autowired
    private AirQualityStatisticService airQualityStatisticService;

    @RequestMapping(value = "/hourly/cityAqi", method = RequestMethod.POST)
    public ResultData scheduleHourlyCityAqi() {
        return airQualityStatisticService.handleAirQualityHourlyStatistic();
    }

    @RequestMapping(value = "/hourly/cityAqi", method = RequestMethod.GET)
    public ResultData getHourlyCityAqi() {

        return null;
    }

    @RequestMapping(value = "/daily/cityAqi", method = RequestMethod.POST)
    public ResultData scheduleDailyCityAqi() {
        return airQualityStatisticService.handleAirQualityDailyStatistic();
    }

    @RequestMapping(value = "/daily/cityAqi", method = RequestMethod.GET)
    public ResultData getDailyCityAqi() {
        return null;
    }

    @RequestMapping(value = "/monthly/cityAqi", method = RequestMethod.POST)
    public ResultData scheduleMonthlyCityAqi() {
        return airQualityStatisticService.handleAirQualityMonthlyStatistic();
    }

    @RequestMapping(value = "/monthly/cityAqi", method = RequestMethod.GET)
    public ResultData getMonthlyCityAqi() {
        return null;
    }
}
