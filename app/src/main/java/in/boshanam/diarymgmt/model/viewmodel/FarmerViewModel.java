package in.boshanam.diarymgmt.model.viewmodel;

import android.arch.lifecycle.ViewModel;

import in.boshanam.diarymgmt.model.FarmerDataCache;
import in.boshanam.diarymgmt.model.FarmerLiveData;

/**
 * Created by Siva on 3/3/2018.
 */

public class FarmerViewModel extends ViewModel {
    private FarmerDataCache farmerDataCache;
    private String dairyId;

    public void init(String dairyId) {
        this.dairyId = dairyId;
        farmerDataCache = FarmerDataCache.getInstance(dairyId);
    }

    public FarmerDataCache getFarmerDataCache() {
        return farmerDataCache;
    }

    public FarmerLiveData getFarmerLiveData() {
        if (farmerDataCache != null) {
            return farmerDataCache.getFarmerLiveData();
        }
        throw new IllegalStateException("FarmerViewModel not initialized, call init() before using");
    }
}
