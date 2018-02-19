package in.boshanam.diarymgmt.service;

import android.app.Activity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import in.boshanam.diarymgmt.command.ListenerAdapter;

/**
 * Created by Siva on 2/17/2018.
 */

public class FirestoreQueryPaginator<T> {

    private volatile DocumentSnapshot lastRecord;
    private final Query query;
    private final int pageSize;
    private volatile List<T> page;
    private final Class<T> docClass;

    public FirestoreQueryPaginator(Query query, int pageSize, Class<T> docClass) {
        this.query = query;
        this.pageSize = pageSize;
        this.docClass = docClass;
    }

    public void readNextPage(Activity activity, final ListenerAdapter<List<T>> listenerAdapter) {
        Query nextPageQuery = query;
        if (lastRecord != null) {
            // Construct a new query starting at lastPage last document        }
            nextPageQuery.startAfter(lastRecord);
        }
        nextPageQuery.limit(pageSize);

        Task<QuerySnapshot> querySnapshotTask = nextPageQuery.get();
        //Delegate failure to caller
        querySnapshotTask.addOnFailureListener(listenerAdapter);

        //Optimize below listener to run on background thread instead of UI thread
        querySnapshotTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                // Get the lastRecord visible document
                try {
                    if (documentSnapshots.size() <= 0) {
                        lastRecord = null;
                    } else {
                        lastRecord = documentSnapshots.getDocuments()
                                .get(documentSnapshots.size() - 1);
                    }
                    page = documentSnapshots.toObjects(docClass);
                } catch (Exception e) {
                    listenerAdapter.onFailure(e);
                }
                listenerAdapter.onSuccess(page);
            }
        });
    }
}
