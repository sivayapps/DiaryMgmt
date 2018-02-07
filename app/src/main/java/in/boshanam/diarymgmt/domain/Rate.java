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
    private String milkType;
    private float fat;
    private float rate;
    private Date effectiveDate;

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

    public String getMilkType() {
        return milkType;
    }

    public void setMilkType(String milkType) {
        this.milkType = milkType;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }


}
