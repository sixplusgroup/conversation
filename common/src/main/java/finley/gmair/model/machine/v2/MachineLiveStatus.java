package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class MachineLiveStatus extends Entity {
    private String uid;

    private int pm2_5;

    private int temperature;

    private int humidity;

    private int co2;

    private int volume;

    private PowerStatus pStatus;

    //work mode to be decided
    private WorkMode mode;

    private HeatStatus hStatus;


}
