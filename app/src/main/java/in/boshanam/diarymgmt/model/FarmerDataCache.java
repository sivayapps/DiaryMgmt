package in.boshanam.diarymgmt.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import in.boshanam.diarymgmt.util.StringUtils;

/**
 * Created by Siva on 3/2/2018.
 */

public class FarmerDataCache {

    private static Map<String, FarmerDataCache> INSTANCES = new ConcurrentHashMap<>();

    private FarmerLiveData farmerLiveData;

    public static FarmerDataCache getInstance(String dairyId) {
        if (StringUtils.isNotBlank(dairyId)) {
            synchronized (INSTANCES) {
                FarmerDataCache data = INSTANCES.get(dairyId);
                if (data == null) {
                    data = new FarmerDataCache(dairyId);
                    INSTANCES.put(dairyId, data);
                }
                return data;
            }
        }
        return null;
    }

    private FarmerDataCache(String dairyId) {
        farmerLiveData = new FarmerLiveData(dairyId);
    }

    public FarmerLiveData getFarmerLiveData() {
        return farmerLiveData;
    }
}
