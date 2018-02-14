package in.boshanam.diarymgmt.domain;

import java.util.Date;

import in.boshanam.diarymgmt.util.StringUtils;

/**
 * Created by Siva on 2/10/2018.
 */
public class CollectedMilk {
    private String id;
    private Date date;
    private Shift shift;
    private String dairyId;
    private String farmerId;
    private MilkType milkType;
    private float milkQuantity;
    private float fat;
    private float milkPerLtrPriceOverride;
    private float perLtrPriceUsed;
    private float milkPriceComputed;
    private float lr;
    private int milkSampleNumber;
    private Date updateTime;

    public boolean isValid() {
        if (StringUtils.isNotBlank(dairyId) && StringUtils.isNotBlank(farmerId) && milkQuantity > 0
                && (milkSampleNumber > 0 || (fat > 0 && fat < 10.1)) && date != null) {
            return true;
        }
        return false;
    }

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

    public float getMilkPerLtrPriceOverride() {
        return milkPerLtrPriceOverride;
    }

    public void setMilkPerLtrPriceOverride(float milkPerLtrPriceOverride) {
        this.milkPerLtrPriceOverride = milkPerLtrPriceOverride;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public MilkType getMilkType() {
        return milkType;
    }

    public void setMilkType(MilkType milkType) {
        this.milkType = milkType;
    }

    public float getPerLtrPriceUsed() {
        return perLtrPriceUsed;
    }

    public void setPerLtrPriceUsed(float perLtrPriceUsed) {
        this.perLtrPriceUsed = perLtrPriceUsed;
    }

    public float getMilkPriceComputed() {
        return milkPriceComputed;
    }

    public void setMilkPriceComputed(float milkPriceComputed) {
        this.milkPriceComputed = milkPriceComputed;
    }

    public float getLr() {
        return lr;
    }

    public void setLr(float lr) {
        this.lr = lr;
    }
}
