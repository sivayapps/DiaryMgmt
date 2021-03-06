package in.boshanam.diarymgmt.domain;

import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

/**
 * Created by user on 2/2/2018.
 */

public class Rate {
    private String id;
    private String dairyId;
    private MilkType milkType;
    private float fat;
    private float price;
    private Date effectiveDate;
    private Date updateTime;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDairyId() {
        return dairyId;
    }

    public void setDairyId(String dairyId) {
        this.dairyId = dairyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public MilkType getMilkType() {
        return milkType;
    }

    public void setMilkType(MilkType milkType) {
        this.milkType = milkType;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "id='" + id + '\'' +
                ", dairyId='" + dairyId + '\'' +
                ", milkType=" + milkType +
                ", fat=" + fat +
                ", price=" + price +
                ", effectiveDate=" + effectiveDate +
                ", updateTime=" + updateTime +
                ", active=" + active +
                '}';
    }
}
