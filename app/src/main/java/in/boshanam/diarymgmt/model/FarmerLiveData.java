package in.boshanam.diarymgmt.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.boshanam.diarymgmt.domain.Farmer;
import in.boshanam.diarymgmt.repository.FireBaseDao;
import in.boshanam.diarymgmt.repository.FirebaseQueryLiveData;

/**
 * Created by Siva on 3/2/2018.
 */

public class FarmerLiveData extends FirebaseQueryLiveData<Farmer> {

    private final MutableLiveData<List<DocumentSnapshot>> farmerDocSnapshotLiveData;
    private final LiveData<Map<String, Farmer>> farmerIdFarmerCacheLiveData;

    public FarmerLiveData(String dairyId) {
        super(FireBaseDao.buildAllFarmersQuery(dairyId), Farmer.class);
        this.farmerDocSnapshotLiveData = new MutableLiveData<>();
        this.farmerIdFarmerCacheLiveData = Transformations.map(this, farmers -> {
            Map<String, Farmer> data = new HashMap<>();
            for (Farmer farmer : farmers) {
                data.put(farmer.getId(), farmer);
            }
            return data;
        });
    }

    @Override
    public List<Farmer> transformSnapshotToData(List<DocumentSnapshot> documentSnapshots) {
        List<Farmer> farmers = super.transformSnapshotToData(documentSnapshots);

        return farmers;
    }

    @Override
    protected void postRawData(List<DocumentSnapshot> documentSnapshots) {
        farmerDocSnapshotLiveData.postValue(documentSnapshots);
    }

    public MutableLiveData<List<DocumentSnapshot>> getFarmerDocSnapshotLiveData() {
        return farmerDocSnapshotLiveData;
    }

    public LiveData<Map<String, Farmer>> getFarmerIdFarmerCacheLiveData() {
        return farmerIdFarmerCacheLiveData;
    }
}
