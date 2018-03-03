package in.boshanam.diarymgmt.repository;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import in.boshanam.diarymgmt.app.AppExecutors;

/**
 * Created by Siva on 2/23/2018.
 */

public class FirebaseQueryLiveData<T> extends LiveData<List<T>> {
    private static final String TAG = "FirebaseQueryLiveData";

    protected final Query query;
    protected final Class<T> clazz;
    protected int observerCount;

    protected ListenerRegistration listenerRegistration;

    public FirebaseQueryLiveData(Query query, Class<T> clazz) {
        this.query = query;
        this.clazz = clazz;
    }

    public FirebaseQueryLiveData(Query query) {
        this(query, null);
    }

    @Override
    protected void onActive() {
        observerCount++;
        if (listenerRegistration != null) {
            Log.d(TAG, "onActive - Already Listening for snapshot: Observer Count: " + observerCount);
            return;
        }
        Log.d(TAG, "onActive: Observer Count: " + observerCount);
        listenerRegistration = query.addSnapshotListener(AppExecutors.getInstance().networkIO(),
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                        List<DocumentSnapshot> documentSnapshots = querySnapshot.getDocuments();
                        postQuerySnapshot(querySnapshot);
                        postRawData(documentSnapshots);
                        List<T> value = transformSnapshotToData(documentSnapshots);
                        postValue(value);
                    }
                });
    }

    @Override
    protected void onInactive() {
        observerCount--;
        Log.d(TAG, "onInactive: Observer Count: " + observerCount);
        if (listenerRegistration != null && observerCount <= 0) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }

    public List<T> transformSnapshotToData(List<DocumentSnapshot> documentSnapshots) {
        if (clazz == null) {
            return (List<T>) documentSnapshots;
        } else {
            List<T> data = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                data.add(documentSnapshot.toObject(clazz));
            }
            return data;
        }
    }

    /**
     * @param querySnapshot
     */
    protected void postQuerySnapshot(QuerySnapshot querySnapshot) {

    }

    /**
     * this data receivers are suggested to copy list if modification to list is required
     *
     * @param documentSnapshots
     */
    protected void postRawData(List<DocumentSnapshot> documentSnapshots) {

    }
}