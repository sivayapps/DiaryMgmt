package in.boshanam.diarymgmt.command;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Siva on 2/7/2018.
 */

public abstract class ListenerAdapter<T> implements OnSuccessListener<T>, OnFailureListener, OnCompleteListener<T> {

    @Override
    public void onComplete(@NonNull Task<T> task) {

    }

    @Override
    public void onFailure(@NonNull Exception e) {

    }

    @Override
    public void onSuccess(T t) {

    }
}
