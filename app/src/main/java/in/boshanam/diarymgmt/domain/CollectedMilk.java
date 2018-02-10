package in.boshanam.diarymgmt.domain;

import java.util.Date;

/**
 * Created by Siva on 2/10/2018.
 */
public class CollectedMilk {
    private String id;
    private Date date;
    private String dairyId;
    private String farmerId;
    private float milkQuantity;
    private float fat;
    private int milkSampleNumber;
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDairyId() {
        return dairyId;
    }

    public void setDairyId(String dairyId) {
        this.dairyId = dairyId;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public float getMilkQuantity() {
        return milkQuantity;
    }

    public void setMilkQuantity(float milkQuantity) {
        this.milkQuantity = milkQuantity;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public int getMilkSampleNumber() {
        return milkSampleNumber;
    }

    public void setMilkSampleNumber(int milkSampleNumber) {
        this.milkSampleNumber = milkSampleNumber;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
