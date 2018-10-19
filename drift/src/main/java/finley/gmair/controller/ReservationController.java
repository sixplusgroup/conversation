package finley.gmair.controller;

import finley.gmair.form.drift.ReservationForm;
import finley.gmair.model.drift.Reservation;
import finley.gmair.service.ReservationService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/drift/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping(value = "/create")
    public ResultData createReservation(ReservationForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getConsumerId()) || StringUtils.isEmpty(form.getGoodsId()) || StringUtils.isEmpty(form.getExpected())
            || StringUtils.isEmpty(form.getInterval()) || StringUtils.isEmpty(form.getConsigneeName()) || StringUtils.isEmpty(form.getConsigneePhone())
            || StringUtils.isEmpty(form.getConsigneeAddress()) || StringUtils.isEmpty(form.getProvinceId()) || StringUtils.isEmpty(form.getCityId())
            || StringUtils.isEmpty(form.getTestTarget())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill all the required fields");
            return result;
        }

        String consumerId = form.getConsumerId().trim();
        String goodsId = form.getGoodsId().trim();
        Date expected = form.getExpected();
        int interval = form.getInterval();
        String consigneeName = form.getConsigneeName().trim();
        String consigneePhone = form.getConsigneePhone().trim();
        String consigneeAddress = form.getConsigneeAddress().trim();
        String provinceId = form.getProvinceId().trim();
        String cityId = form.getCityId().trim();
        String testTarget = form.getTestTarget().trim();

        Reservation reservation = new Reservation(consumerId, goodsId, expected, interval, consigneeName,
                consigneePhone, consigneeAddress, provinceId, cityId, testTarget);

        ResultData response = reservationService.createReservation(reservation);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store reservation to database");
        }
        return result;
    }

    @GetMapping(value = "/list")
    public ResultData getReservation() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = reservationService.fetchReservation(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Query error");
                break;
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No reservation found");
                break;
        }
        return result;
    }
}
