package in.boshanam.diarymgmt.domain;

import java.util.Date;

/**
 * Created by Shiva on 2/7/2018.
 *
 */

public class Milk {

    private String id;
    private String dairyId;
    private String farmerId;
    private int sampleNo;
    private float collectMilk;
    private Date collectDate;

    public Milk(){

    }
    public Milk(String farmerId, int sampleNo, float collectMilk) {
        this.farmerId=farmerId;
        this.sampleNo=sampleNo;
        this.collectMilk=collectMilk;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getDairyId() {
        return dairyId;
    }

    public void setDairyId(String dairyId) {
        this.dairyId = dairyId;
    }
    public Date getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(Date collectDate) {
        this.collectDate = collectDate;
    }
    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public int getSampleNo() {
        return sampleNo;
    }

    public void setSampleNo(int sampleNo) {
        this.sampleNo = sampleNo;
    }

    public float getCollectMilk() {
        return collectMilk;
    }

    public void setCollectMilk(float collectMilk) {
        this.collectMilk = collectMilk;
    }
}
