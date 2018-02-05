package in.boshanam.diarymgmt.repository;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import in.boshanam.diarymgmt.FarmerActivity;
import in.boshanam.diarymgmt.domain.DairyOwner;
import in.boshanam.diarymgmt.domain.Farmer;

/**
 * Created by Siva on 2/2/2018.
 */

public class FireBaseDao {

    private FireBaseDao() {
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
                        if (dairyOwner.getDairyIds() != null && dairyOwner.getDairyIds().size() > 0
                                && dairyOwner.getName() != null) {
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

    public static void saveDairyOwner(DairyOwner dairyOwner, final AppCompatActivity activity,
                                      final Runnable successCommand, final Runnable failureCommand) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = dairyOwner.getUid();
        DocumentReference dairyOwnerProfileRef = db.collection("DairyOwnerProfile").document(userId);
        dairyOwnerProfileRef.set(dairyOwner, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                successCommand.run();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                failureCommand.run();
            }
        });
    }

    public static void saveFarmer(Farmer farmer, final AppCompatActivity farmerActivity, final Runnable successCommand, final Runnable failureCommand) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = farmer.getId();
        DocumentReference farmerRegistrationRef = db.collection("FarmerRegistration").document(userId);
        farmerRegistrationRef.set(farmer, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                successCommand.run();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                failureCommand.run();
            }
        });
    }
}
