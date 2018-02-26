package in.boshanam.diarymgmt.tableview.model;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import in.boshanam.diarymgmt.app.constants.GridBaseEnum;
import in.boshanam.diarymgmt.app.constants.GridColumnType;

/**
 * Created by Siva on 2/25/2018.
 */

public class TableViewModelDef<T, E extends Enum<E> & GridBaseEnum> {

    private final Class<E> gridConfigEnumClass;
    private final E[] columnsDef;

    public TableViewModelDef(Class<E> gridConfigEnumClass) {
        this.gridConfigEnumClass = gridConfigEnumClass;
        //Get Grid header definition enums
        this.columnsDef = gridConfigEnumClass.getEnumConstants();
    }

    public String getRowDisplayId(T row, int rowNum) {
        return (rowNum + 1) + "";
    }

    public String getCelId(T row, int rowNum, int colNum) {
        return "r" + rowNum + "c" + colNum;
    }

    public Object getFieldValue(T row, E colDef) {
        if (row instanceof DocumentSnapshot) {
            return ((DocumentSnapshot) row).get(colDef.getFieldName());
        }
        return ((List<Object>) row).get(colDef.ordinal());
    }

    public E[] getColumnsDef() {
        return columnsDef;
    }

    public E getColumnDef(int colNum) {
        if (colNum >= 0 && colNum < columnsDef.length) {
            return columnsDef[colNum];
        }
        return null;
    }

    public GridColumnType getColumnType(int colNum) {
        E columnDef = getColumnDef(colNum);
        if (columnDef != null) {
            return columnDef.getColumnType();
        }
        return null;
    }
}
