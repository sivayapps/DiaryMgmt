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

import in.boshanam.diarymgmt.command.ListenerAdapter;
import in.boshanam.diarymgmt.domain.Dairy;
import in.boshanam.diarymgmt.domain.DairyOwner;
import in.boshanam.diarymgmt.domain.Farmer;
import in.boshanam.diarymgmt.util.StringUtils;

/**
 * Created by Siva on 2/2/2018.
 */
public class FireBaseDao {

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

    public static void onDairyOwnerProfileStatusValidation(FirebaseUser user, final AppCompatActivity activity,
                                                           final Runnable successCommand, final Runnable failureCommand) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = user.getUid();
        DocumentReference dairyOwnerProfileRef = db.collection("DairyOwnerProfile").document(userId);
        dairyOwnerProfileRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        DairyOwner dairyOwner = document.toObject(DairyOwner.class);
                        if (StringUtils.isNotBlank(dairyOwner.getDairyName()) && StringUtils.isNotBlank(dairyOwner.getName())) {
                            Log.d("DEBUG", "Owner Profile Complete - DocumentSnapshot data: " + dairyOwner);
                            successCommand.run();
                            return;
                        }
                        Log.d("DEBUG", "Owner Profile Not Complete - DocumentSnapshot data: " + dairyOwner);
                    } else {
                        Log.d("DEBUG", "Owner Profile not found");
                    }
                    failureCommand.run();
                } else {
                    Log.d("DEBUG", "get failed with ", task.getException());
                    Toast.makeText(activity, "Data Retrieval failed with " + task.getException(), Toast.LENGTH_LONG);
                }
            }
        });
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
        }).addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listenerCommand.onFailure(e);
            }
        });
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
            dairyRef = db.collection("Dairy").document(dairyOwner.getDairyId());
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = farmer.getId();
        DocumentReference farmerRegistrationRef;
        if (StringUtils.isNotBlank(userId)) {
            farmerRegistrationRef = db.collection("Farmer").document(userId);
        } else {
            farmerRegistrationRef = db.collection("Farmer").document();
            farmer.setId(farmerRegistrationRef.getId());
        }
        farmerRegistrationRef.set(farmer, SetOptions.merge())
                .addOnSuccessListener(activity, listenerAdapter)
                .addOnFailureListener(activity, listenerAdapter);
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
}
