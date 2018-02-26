package in.boshanam.diarymgmt.tableview.model;

import com.evrencoskun.tableview.sort.ISortableModel;


/**
 * Created by Siva on 2/25/2018.
 */
public class CellModel implements ISortableModel {
    private String cellId;
    private Object cellData;

    public CellModel(String cellId, Object cellData) {
        this.cellId = cellId;
        this.cellData = cellData;
    }

    public Object getData() {
        return cellData;
    }

    @Override
    public String getId() {
        return cellId;
    }

    @Override
    public Object getContent() {
        return cellData;
    }

}
