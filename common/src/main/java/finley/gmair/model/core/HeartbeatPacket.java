package finley.gmair.model.core;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class HeartbeatPacket {

    private String FRH;

    private String CTF;

    private String CID;

    private String UID;

    private long TIME;

    private String LEN;

    private String DATA;

    private String CRC;

    private String FRT;

    public HeartbeatPacket(){

    }

    public HeartbeatPacket(String FRH, String CTF, String CID, String UID, long TIME, String LEN, String DATA, String CRC, String FRT){
        this();
        this.FRH=FRH;
        this.CTF=CTF;
        this.CID=CID;
        this.UID=UID;
        this.TIME=TIME;
        this.LEN=LEN;
        this.DATA=DATA;
        this.CRC=CRC;
        this.FRT=FRT;
    }

    public String getFRH() {
        return FRH;
    }

    public void setFRH(String FRH) {
        this.FRH = FRH;
    }

    public String getCTF() {
        return CTF;
    }

    public void setCTF(String CTF) {
        this.CTF = CTF;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public long getTIME() {
        return TIME;
    }

    public void setTIME(long TIME) {
        this.TIME = TIME;
    }

    public String getLEN() {
        return LEN;
    }

    public void setLEN(String LEN) {
        this.LEN = LEN;
    }

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public String getCRC() {
        return CRC;
    }

    public void setCRC(String CRC) {
        this.CRC = CRC;
    }

    public String getFRT() {
        return FRT;
    }

    public void setFRT(String FRT) {
        this.FRT = FRT;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,SerializerFeature.DisableCircularReferenceDetect);
    }
}
