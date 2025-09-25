package se.urmo.mqttconnector.model;


public class BaseLocation {
    private String tid;
    private int batt;
    private int acc;
    private String con;
    private Double lat;
    private Double lon;
    private String tst;

    public BaseLocation() {

    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public int getBatt() {
        return batt;
    }

    public void setBatt(int batt) {
        this.batt = batt;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getTst() {
        return tst;
    }

    public void setTst(String tst) {
        this.tst = tst;
    }

    public int getAcc() {
        return acc;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }
}
