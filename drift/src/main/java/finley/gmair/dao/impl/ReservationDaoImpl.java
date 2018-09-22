package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ReservationDao;
import finley.gmair.model.drift.Reservation;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ReservationDaoImpl extends BaseDao implements ReservationDao {
    @Override
    public ResultData queryReservation(Map<String, Object> condition) {
        return null;
    }

    @Override
    public ResultData insertReservation(Reservation reservation) {
        return null;
    }

    @Override
    public ResultData updateReservation(Reservation reservation) {
        return null;
    }
}
