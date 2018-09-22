package finley.gmair.dao;

import finley.gmair.model.drift.Reservation;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ReservationDao {
    ResultData queryReservation(Map<String, Object> condition);

    ResultData insertReservation(Reservation reservation);

    ResultData updateReservation(Reservation reservation);
}
