package in.boshanam.diarymgmt.tableview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import in.boshanam.diarymgmt.app.constants.GridBaseEnum;
import in.boshanam.diarymgmt.command.ListenerAdapter;
import in.boshanam.diarymgmt.repository.FireBaseDao;
import in.boshanam.diarymgmt.tableview.model.CellModel;
import in.boshanam.diarymgmt.tableview.model.ColumnHeaderModel;
import in.boshanam.diarymgmt.tableview.model.RowHeaderModel;
import in.boshanam.diarymgmt.tableview.model.TableViewModelDef;

import static in.boshanam.diarymgmt.FarmerActivity.TAG;

/**
 * Created by Siva on 2/25/2018.
 */

public class TableViewHelper<T, E extends Enum<E> & GridBaseEnum> {

    private final TableView tableView;
    private final TableViewModelDef<T, E> tableViewModelDef;
    private final TableAdapter tableAdapter;
    private List<T> rowsData;
    private Activity context;
    private ListenerRegistration listenerRegistration;

    private TableViewHelper(Activity context, TableView tableView, TableViewModelDef<T, E> tableViewModelDef) {
        this.tableView = tableView;
        this.context = context;
        this.tableViewModelDef = tableViewModelDef;

        // Create TableView Adapter
        this.tableAdapter = new TableAdapter(context, tableViewModelDef);
        tableView.setAdapter(this.tableAdapter);
    }

    public static <T, E extends Enum<E> & GridBaseEnum>
    TableViewHelper buildTableViewHelper(Activity context, TableView tableView,
                                         TableViewModelDef<T, E> tableViewModelDef,
                                         boolean enableDelete) {
        TableViewHelper tableViewHelper = new TableViewHelper(context, tableView, tableViewModelDef);
        tableView.setTableViewListener(new TableViewListener(tableView, tableViewHelper, enableDelete));
        tableView.setFocusable(false);
        return tableViewHelper;
    }

    public ListenerRegistration initGridWithQuerySnapshot(final Activity context, final Query dataQuery) {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
        //Init Table DataModel
        listenerRegistration = FireBaseDao.registerQuerySnapshotListener(context, dataQuery, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshot, FirebaseFirestoreException e) {
                rowsData = null;
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    Toast.makeText(context, "Failed loading Data-" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    //Clear data in table, don't show old data
                    initTableWithData(context, new ArrayList<>());
                    return;
                }
//                List<T> rowsDataListObj = (List<T>) extractRowData(documentSnapshot.getDocuments(), tableViewModelDef.getColumnsDef());
                List<DocumentSnapshot> documents = documentSnapshot.getDocuments();
                initTableWithData(context, (List<T>) documents);
            }
        });
        return listenerRegistration;
    }

    public void initTableWithData(final Activity context, final List<T> rowsData) {
        //Get Grid header definition enums
        this.rowsData = rowsData;
        if (rowsData == null || rowsData.size() == 0) {
            tableView.setVisibility(View.GONE);
        } else {
            tableView.setVisibility(View.VISIBLE);
        }

        final E[] enumConstants = tableViewModelDef.getColumnsDef();
        List<ColumnHeaderModel> headers = getHeaders(context, enumConstants);
        List<RowHeaderModel> rowHeaders = createRowHeaderList(rowsData);
        List<List<CellModel>> cellModel = buildCellModelList(rowsData);

        // Set all items to the TableView
        tableAdapter.setAllItems(headers, rowHeaders, cellModel);
    }

    private List<List<CellModel>> buildCellModelList(List<T> data) {
        List<List<CellModel>> rows = new ArrayList<>();
        for (int rowNum = 0; rowNum < data.size(); rowNum++) {
            T rowData = data.get(rowNum);
            ArrayList<CellModel> row = new ArrayList<>();
            rows.add(row);
            final E[] enumConstants = tableViewModelDef.getColumnsDef();
            for (E columnDef : enumConstants) {
                Object fieldValue = tableViewModelDef.getFieldValue(rowData, columnDef);
                row.add(new CellModel(tableViewModelDef.getCelId(rowData, rowNum,
                        columnDef.ordinal()), columnDef.transformValue(fieldValue)));
            }
        }
        return rows;
    }

    private List<RowHeaderModel> createRowHeaderList(List<T> rowData) {
        List<RowHeaderModel> list = new ArrayList<>();
        for (int rowNum = 0; rowNum < rowData.size(); rowNum++) {
            list.add(new RowHeaderModel(tableViewModelDef.getRowDisplayId(rowData.get(rowNum), rowNum)));
        }
        return list;
    }

    public List<ColumnHeaderModel> getHeaders(final Context context, E[] enumConstants) {
        List<ColumnHeaderModel> list = new ArrayList<>();
        for (E columnDef : enumConstants) {
            String header = context.getString(columnDef.getColumnHeader());
            list.add(new ColumnHeaderModel(header));
        }
        return list;
    }

    public TableViewModelDef<T, E> getTableViewModelDef() {
        return tableViewModelDef;
    }

    public T getRow(int rowNum) {
        if (rowNum >= 0 && rowNum < rowsData.size()) {
            return rowsData.get(rowNum);
        }
        return null;
    }

    /**
     * By default handles DocumentSnapshot as the row, if any other row type,
     * this method has to be overridden if the row type is not DocumentSnapshot
     *
     * @param rowNum
     */
    public void deleteRow(int rowNum) {
        T row = getRow(rowNum);
        if (row == null) {
            return;
        }
        //Default handling only supports firebase document reference
        if (row instanceof DocumentSnapshot) {
            DocumentSnapshot documentSnapshot = (DocumentSnapshot) row;
            DocumentReference ref = documentSnapshot.getReference();
            ListenerAdapter<Void> listener = new ListenerAdapter<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Deleted row", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Exception while deleting row: ", e);
                    Toast.makeText(context, "Exception while deleting row: "
                            + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            };
            ref.delete().addOnSuccessListener(context, listener).addOnFailureListener(listener);
        }
    }
}
