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

    public static <E extends Enum<E> & GridBaseEnum>
    ListenerRegistration initGridWithQuerySnapshot(final Activity context,
                                                   final TableView<String[]> tableView,
                                                   final Class<E> gridConfigEnumClass,
                                                   final Query dataQuery) {
        return initGridWithQuerySnapshot(context, tableView, gridConfigEnumClass, dataQuery, null);
    }

    public static <E extends Enum<E> & GridBaseEnum>
    ListenerRegistration initGridWithQuerySnapshot(final Activity context,
                                                   final TableView<String[]> tableView,
                                                   final Class<E> gridConfigEnumClass,
                                                   final Query dataQuery, final TableColumnModel columnModel) {
        return initGridWithQuerySnapshot(context, tableView, gridConfigEnumClass, dataQuery, null, false);

    }

    public static <E extends Enum<E> & GridBaseEnum>
    ListenerRegistration initGridWithQuerySnapshot(final Activity context,
                                                   final TableView<String[]> tableView,
                                                   final Class<E> gridConfigEnumClass,
                                                   final Query dataQuery, final TableColumnModel columnModel,
                                                   final boolean enableSorting) {

        //Get Grid header definition enums
        final E[] enumConstants = gridConfigEnumClass.getEnumConstants();
        final int columnCount = enumConstants.length;
        initGridHeaders(context, tableView, columnModel, enumConstants, columnCount);

        //Init Table DataModel
        return FireBaseDao.registerQuerySnapshotListener(context, dataQuery, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    Toast.makeText(context, "Failed loading Data-" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                List<Object[]> rowsData = extractRowData(documentSnapshots, enumConstants);
                initGridWithDataAdaptor(context, tableView, rowsData, enumConstants, enableSorting);
            }

        });
    }

    public static <E extends Enum<E> & GridBaseEnum>
    void initGridWithData(final Activity context,
                          final TableView<String[]> tableView,
                          final Class<E> gridConfigEnumClass,
                          final List<Object[]> rowsData, final TableColumnModel columnModel,
                          final boolean enableSorting) {

        //Get Grid header definition enums
        final E[] enumConstants = gridConfigEnumClass.getEnumConstants();
        final int columnCount = enumConstants.length;
        initGridHeaders(context, tableView, columnModel, enumConstants, columnCount);
        initGridWithDataAdaptor(context, tableView, rowsData, enumConstants, enableSorting);

    }

    public static <E extends Enum<E> & GridBaseEnum>
    void initGridWithDataAdaptor(final Activity context, final TableView<String[]> tableView,
                                 List<Object[]> rowsData, E[] enumConstants, boolean enableSorting) {
        SimpleDateFormat displayDateFormat = null;
        List<String[]> rows = new ArrayList<>();
        String[] row;
        for (Object[] rowData : rowsData) {
            row = new String[enumConstants.length];
            rows.add(row);
            for (E columnDef : enumConstants) {
                Object value = rowData[columnDef.ordinal()];
                if (value != null) {
                    if (enableSorting) {
                        setComparator(tableView, columnDef);
                    }
                    String formattedValue = null;
                    if (columnDef.getColumnType() == DATE && value instanceof Date) {
                        if (displayDateFormat == null) {
                            displayDateFormat = new SimpleDateFormat(AppConstants.DISPLAY_GRID_DATE_FORMAT, Locale.getDefault());
                        }
                        formattedValue = displayDateFormat.format(value);
                    } else if (columnDef.getColumnType() == GridColumnType.ENUM) {
                        int resourceIdForValue = columnDef.getResourceIdForValue(value.toString());
                        if (resourceIdForValue != -1) {
                            formattedValue = context.getString(resourceIdForValue);
                        }
                    } else if (StringUtils.isNotBlank(columnDef.getFormatString())) {
                        String formatString = columnDef.getFormatString();
                        if (value != null && value.toString().trim().length() > 0) {
                            formattedValue = String.format(formatString, value);
                        } else {
                            formattedValue = "" + value;
                        }
                    }
                    if (StringUtils.isBlank(formattedValue)) {
                        formattedValue = String.valueOf(value);
                    }
                    row[columnDef.ordinal()] = formattedValue;
                } else {
                    row[columnDef.ordinal()] = "";
                }
            }
        }
        CustomTableDataAdapter dataAdapter = new CustomTableDataAdapter(context, rows, enumConstants);
        dataAdapter.setTextColor(context.getResources().getColor(R.color.textColorPrimary));
        dataAdapter.setTextSize(10);
        tableView.setDataAdapter(dataAdapter);
    }

    @NonNull
    public static <E extends Enum<E> & GridBaseEnum> List<Object[]> extractRowData(QuerySnapshot documentSnapshots,
                                                                                   E[] enumConstants) {
        List<Object[]> rowsData = new ArrayList<>();
        for (DocumentSnapshot dc : documentSnapshots.getDocuments()) {
            Object[] rowData = new Object[enumConstants.length];
            for (E columnDef : enumConstants) {
                Object value = dc.get(columnDef.getFieldName());
                if (value != null) {
                    rowData[columnDef.ordinal()] = value;
                } else {
                    rowData[columnDef.ordinal()] = "";
                }
            }
            rowsData.add(rowData);
        }
        return rowsData;
    }

    private static <E extends Enum<E> & GridBaseEnum> void initGridHeaders(Activity context, TableView<String[]> tableView, TableColumnModel columnModel, E[] enumConstants, int columnCount) {
        //Setup TableHeaderModel
        if (columnModel != null) {
            tableView.setColumnModel(columnModel);
        } else {
            tableView.setColumnCount(columnCount);
        }
        SimpleTableHeaderAdapter headerAdapter = new SimpleTableHeaderAdapter(context, getHeaders(enumConstants));
        headerAdapter.setTextColor(context.getResources().getColor(R.color.textColorPrimary));
        headerAdapter.setTextSize(12);
        tableView.setHeaderAdapter(headerAdapter);
    }

    private static <E extends Enum<E> & GridBaseEnum> void setComparator(TableView<String[]> tableView, E columnDef) {
        if (tableView instanceof SortableTableView) {
            SortableTableView sortableTable = (SortableTableView) tableView;
            switch (columnDef.getColumnType()) {
                case STRING:
                    sortableTable.setColumnComparator(columnDef.ordinal(), new StringComparator(columnDef.ordinal()));
                    break;
                case DATE:
                    sortableTable.setColumnComparator(columnDef.ordinal(), new DateComparator(columnDef.ordinal(), AppConstants.DISPLAY_GRID_DATE_FORMAT));
                    break;
                case NUMBER:
                    sortableTable.setColumnComparator(columnDef.ordinal(), new NumberComparator(columnDef.ordinal()));
                    break;
            }
        }
    }

    public static <E extends Enum<E> & GridBaseEnum> int[] getHeaders(E[] enumConstants) {
        int[] headers = new int[enumConstants.length];
        for (E columnDef : enumConstants) {
            headers[columnDef.ordinal()] = columnDef.getColumnHeader();
        }
        return headers;
    }
}
