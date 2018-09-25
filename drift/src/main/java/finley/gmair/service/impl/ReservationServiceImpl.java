package finley.gmair.service.impl;

import finley.gmair.dao.ReservationDao;
import finley.gmair.model.drift.Reservation;
import finley.gmair.service.ReservationService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationDao reservationDao;

    @Override
    public ResultData fetchReservation(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = reservationDao.queryReservation(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No reservation found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve reservation");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData createReservation(Reservation reservation) {
        ResultData result = new ResultData();
        ResultData response = reservationDao.insertReservation(reservation);
        switch (response.getResponseCode()) {
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to insert reservation");
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public ResultData modifyReservation(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = reservationDao.updateReservation(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("Succeed to update reservation");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to update reservation");
                break;
            default:
                break;
        }
        return result;
    }
}
