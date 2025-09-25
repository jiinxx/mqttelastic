package se.urmo.mqttconnector.model;

public class MQTTLocation {
    private BaseLocation baseLocation = new BaseLocation();
    private String _type;

    public MQTTLocation() {
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public BaseLocation getBaseLocation() {
        return baseLocation;
    }

    public String getTid() {
        return baseLocation.getTid();
    }

    public void setTid(String tid) {
        baseLocation.setTid(tid);
    }

    public int getBatt() {
        return baseLocation.getBatt();
    }

    public void setBatt(int batt) {
        baseLocation.setBatt(batt);
    }

    public String getConn() {
        return baseLocation.getCon();
    }

    public void setConn(String con) {
        baseLocation.setCon(con);
    }

    public Double getLat() {
        return baseLocation.getLat();
    }

    public void setLat(Double lat) {
        baseLocation.setLat(lat);
    }

    public Double getLon() {
        return baseLocation.getLon();
    }

    public void setLon(Double lon) {
        baseLocation.setLon(lon);
    }

    public String getTst() {
        return baseLocation.getTst();
    }

    public void setTst(String tst) {
        baseLocation.setTst(tst);
    }

    public void setAcc(int acc){
        baseLocation.setAcc(acc);
    }
    public int getAcc(){
        return baseLocation.getAcc();
    }
}
