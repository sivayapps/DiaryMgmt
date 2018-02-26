package in.boshanam.diarymgmt.repository;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by Siva on 2/23/2018.
 */

public class FirebaseQueryLiveData<T> extends LiveData<T> {
    private static final String LOG_TAG = "FirebaseQueryLiveData";

    private final Query query;

    private ListenerRegistration listenerRegistration;

    public FirebaseQueryLiveData(Query query) {
        this.query = query;
    }

    @Override
    protected void onActive() {
        Log.d(LOG_TAG, "onActive");
        listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                setValue(null);//TODO set data
            }
        });
    }

    @Override
    protected void onInactive() {
        Log.d(LOG_TAG, "onInactive");
        listenerRegistration.remove();
    }

}