package in.boshanam.diarymgmt.repository;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.Date;

import in.boshanam.diarymgmt.FarmerActivity;
import in.boshanam.diarymgmt.command.ListenerAdapter;
import in.boshanam.diarymgmt.domain.Dairy;
import in.boshanam.diarymgmt.domain.DairyOwner;
import in.boshanam.diarymgmt.domain.Farmer;
import in.boshanam.diarymgmt.domain.Rate;
import in.boshanam.diarymgmt.util.MathUtil;
import in.boshanam.diarymgmt.util.StringUtils;

/**
 * Created by Siva on 2/2/2018.
 */
public class FireBaseDao {

    public static final String DAIRY_COLLECTION = "Dairy";
    public static final String FARMER_COLLECTION = "Farmer";

    private FireBaseDao() {
    }

    private static FireBaseDao instance;

    public synchronized static FireBaseDao getInstance() {
        if (instance == null) {
            setup();
            instance = new FireBaseDao();
        }
        return instance;
    }

    public static void setup() {
        // [START get_firestore_instance]
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // [END get_firestore_instance]

        // [START set_firestore_settings]
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        // [END set_firestore_settings]
    }

    public static void onDairyOwnerProfileStatusValidation(Activity activity, FirebaseUser user,
                                                           final ListenerAdapter listenerCommand) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = user.getUid();
        DocumentReference dairyOwnerProfileRef = db.collection("DairyOwnerProfile").document(userId);
        dairyOwnerProfileRef.get().addOnSuccessListener(listenerCommand).addOnFailureListener(listenerCommand);
    }

    public static void findDairyOwner(Activity activity, String dairyOwnerUid,
                                      final ListenerAdapter listenerCommand) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference dairyOwnerProfileRef = db.collection("DairyOwnerProfile").document(dairyOwnerUid);
        Task<DocumentSnapshot> documentSnapshotTask = dairyOwnerProfileRef.get();
        documentSnapshotTask.addOnSuccessListener(activity, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    listenerCommand.onSuccess(documentSnapshot.toObject(DairyOwner.class));
                } else {
                    listenerCommand.onSuccess(null);
                }
            }
        }).addOnFailureListener(activity, listenerCommand);
    }


    public static void saveDairyOwner(Activity activity, DairyOwner dairyOwner,
                                      final ListenerAdapter listenerCommand) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get a new write batch
        WriteBatch batch = db.batch();

        String userId = dairyOwner.getUid();
        DocumentReference dairyRef = null;
//        Task<Void> dairyAddOrUpdateTask;
        if (StringUtils.isNotBlank(dairyOwner.getDairyId())) {
            dairyRef = db.collection(DAIRY_COLLECTION).document(dairyOwner.getDairyId());
//            dairyAddOrUpdateTask = dairyRef.update("dairyName", dairyOwner.getDairyName());
            batch.update(dairyRef, "dairyName", dairyOwner.getDairyName());
        } else {
            dairyRef = db.collection("Dairy").document();
            dairyOwner.setDairyId(dairyRef.getId());
            Dairy dairy = new Dairy();
            dairy.setActive(true);
            dairy.setDairyName(dairyOwner.getDairyName());
            dairy.setId(dairyRef.getId());
            dairy.setUpdateTime(new Date());
//            dairyAddOrUpdateTask = dairyRef.set(dairy, SetOptions.merge());
            batch.set(dairyRef, dairy);
        }
        final DocumentReference dairyOwnerProfileRef = db.collection("DairyOwnerProfile").document(userId);
//        Task<Void> addOrUpdateDairyOwner = dairyOwnerProfileRef.set(dairyOwner, SetOptions.merge());
        batch.set(dairyOwnerProfileRef, dairyOwner, SetOptions.merge());

        batch.commit().addOnSuccessListener(activity, listenerCommand)
                .addOnFailureListener(activity, listenerCommand);
    }

    public static void saveFarmer(Activity activity, Farmer farmer, ListenerAdapter listenerAdapter) {
        String farmerId = farmer.getId();
        String dairyId = farmer.getDairyId();
        if(StringUtils.isBlank(dairyId) || StringUtils.isBlank(farmerId)) {
            throw new IllegalArgumentException("dairyID and FarmerID should not be null");
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference farmerRegistrationRef = db.collection(DAIRY_COLLECTION).document(dairyId)
                .collection(FARMER_COLLECTION).document(farmerId);

        farmerRegistrationRef.set(farmer, SetOptions.merge())
                .addOnSuccessListener(activity, listenerAdapter)
                .addOnFailureListener(activity, listenerAdapter);
    }

    public static void saveRate(Activity activity, final Rate rate, ListenerAdapter listenerAdapter) {
        //TODO revisit method - this is incomplete
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String rateId = rate.getId();
        DocumentReference farmerRegistrationRef;
        if (StringUtils.isNotBlank(rateId)) {
            farmerRegistrationRef = db.collection("Rate")
                    .document(rateId);
        } else {
            Query query = buildRateEntryQuery(rate);
            query.get().addOnSuccessListener(activity, listenerAdapter)
                    .addOnFailureListener(activity, listenerAdapter);
        }
    }

    private static Query buildRateEntryQuery(Rate rate) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("Rate")
                        .whereEqualTo("dairyId", rate.getDairyId())
                        .whereEqualTo("milkType", rate.getMilkType())
                        .whereEqualTo("effectiveDate", rate.getEffectiveDate())
                        .whereGreaterThanOrEqualTo("fat", (rate.getFat() - MathUtil.EPS))
                        .whereLessThan("fat", (rate.getFat() + MathUtil.EPS))
                        .orderBy("fat");
    }

    /**
     * TODO READ https://firebase.google.com/docs/firestore/query-data/listen
     * Use snapshot listener to refresh the grid on every update on data in event handler
     */
    private static Query buildRateEntriesQuery(String dairyId, String milkType, Date effectiveDate) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("Rate")
                        .whereEqualTo("dairyId", dairyId)
                        .whereEqualTo("milkType", milkType)
                        .whereEqualTo("effectiveDate", effectiveDate)
                        .orderBy("fat");
    }

    public ListenerRegistration registerSnapshotListener(Activity activity, Query query, EventListener<QuerySnapshot> snapshotEventListener) {
        ListenerRegistration listenerRegistration = query.addSnapshotListener(activity, snapshotEventListener);
        return listenerRegistration;
    }

    public void detachListener(ListenerRegistration listenerRegistration) {
        // Stop listening to changes
        listenerRegistration.remove();
        ;
    }

    public static void loadFarmerById(Activity activity, String dairyId, String farmerId, ListenerAdapter<DocumentSnapshot> listenerAdapter) {

        if(StringUtils.isBlank(dairyId) || StringUtils.isBlank(farmerId)) {
            throw new IllegalArgumentException("dairyID and FarmerID should not be null");
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference farmerRegistrationRef = db.collection(DAIRY_COLLECTION).document(dairyId)
                .collection(FARMER_COLLECTION).document(farmerId);

        farmerRegistrationRef.get().addOnSuccessListener(activity, listenerAdapter)
                .addOnFailureListener(activity, listenerAdapter);
    }
}
