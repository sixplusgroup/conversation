package finley.gmair.service;

import finley.gmair.model.drift.Reservation;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ReservationService {

    ResultData fetchReservation(Map<String, Object> condition);

    ResultData createReservation(Reservation reservation);

    ResultData modifyReservation(Map<String, Object> condition);
}
