package finley.gmair.model.drift;

import finley.gmair.model.Entity;

import java.sql.Date;

public class Reservation extends Entity {
    private String reservationId;

    private String consumerId;

    private String goodsId;

    private Date expected;

    private int interval;

    public Reservation() {
        super();
    }

    public Reservation(String consumerId, String goodsId, Date expected, int interval) {
        this();
        this.consumerId = consumerId;
        this.goodsId = goodsId;
        this.expected = expected;
        this.interval = interval;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Date getExpected() {
        return expected;
    }

    public void setExpected(Date expected) {
        this.expected = expected;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
