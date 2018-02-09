package in.boshanam.diarymgmt.util.ui;

import android.app.Activity;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import in.boshanam.diarymgmt.LoginActivity;
import in.boshanam.diarymgmt.R;
import in.boshanam.diarymgmt.app.constants.GridBaseEnum;
import in.boshanam.diarymgmt.repository.FireBaseDao;

import static in.boshanam.diarymgmt.FarmerActivity.TAG;

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

    public static void setSpinnerSelectedValue(Spinner spin, String value) {

        for (int i = 0; i < spin.getCount(); i++) {
            if (spin.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                spin.setSelection(i);
                break;
            }

        }
    }

    public static void setSpinnerSelection(Spinner spin, int index) {
        spin.setSelection(index);
    }

    public static <E extends Enum<E> & GridBaseEnum> ListenerRegistration initGridWithQuerySnapshot(final Activity context,
                                                                                                    final TableView<String[]> tableView,
                                                                                                    final Class<E> gridConfigEnumClass,
                                                                                                    final Query dataQuery) {

        //Get Grid header definition enums
        final E[] enumConstants = gridConfigEnumClass.getEnumConstants();
        final int columnCount = enumConstants.length;

        //Setup TableHeaderModel
        tableView.setColumnCount(columnCount);
        SimpleTableHeaderAdapter headerAdapter = new SimpleTableHeaderAdapter(context, getHeaders(enumConstants));
        headerAdapter.setTextColor(context.getResources().getColor(R.color.textColorPrimary));
        tableView.setHeaderAdapter(headerAdapter);

        //Init Table DataModel
        return FireBaseDao.registerQuerySnapshotListener(context, dataQuery, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    Toast.makeText(context, "Failed loading Data-" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                List<String[]> rows = new ArrayList<>();
                String[] row = null;
                for (DocumentSnapshot dc : documentSnapshots.getDocuments()) {
                    row = new String[columnCount];
                    rows.add(row);
                    for (E columnDef : enumConstants) {
                        row[columnDef.ordinal()] = dc.getString(columnDef.getFieldName());
                    }
                }
                SimpleTableDataAdapter dataAdapter = new SimpleTableDataAdapter(context, rows);
                dataAdapter.setTextColor(context.getResources().getColor(R.color.textColorPrimary));
                tableView.setDataAdapter(dataAdapter);
            }
        });
    }

    public static <E extends Enum<E> & GridBaseEnum> int[] getHeaders(E[] enumConstants) {
        int[] headers = new int[enumConstants.length];
        for (E columnDef : enumConstants) {
            headers[columnDef.ordinal()] = columnDef.getColumnHeader();
        }
        return headers;
    }
}
