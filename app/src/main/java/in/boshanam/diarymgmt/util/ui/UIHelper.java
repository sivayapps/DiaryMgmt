package in.boshanam.diarymgmt.util.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import in.boshanam.diarymgmt.LoginActivity;
import in.boshanam.diarymgmt.R;
import in.boshanam.diarymgmt.app.constants.AppConstants;
import in.boshanam.diarymgmt.app.constants.GridBaseEnum;
import in.boshanam.diarymgmt.app.constants.GridColumnType;
import in.boshanam.diarymgmt.repository.FireBaseDao;
import in.boshanam.diarymgmt.util.DateComparator;
import in.boshanam.diarymgmt.util.NumberComparator;
import in.boshanam.diarymgmt.util.StringComparator;
import in.boshanam.diarymgmt.util.StringUtils;

import static in.boshanam.diarymgmt.FarmerActivity.TAG;
import static in.boshanam.diarymgmt.app.constants.GridColumnType.DATE;

/**
 * Created by Siva on 2/9/2018.
 */

public class UIHelper {

    public static void logoutAndShowLogin(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }
    public static void setSpinnerSelection(Spinner spin, int index) {
        spin.setSelection(index);
    }


}
