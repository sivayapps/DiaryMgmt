package in.boshanam.diarymgmt.domain;

import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by user on 2/2/2018.
 */

public class Rate {
    private String milkType;
    private float fat;
    private float rate;

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
