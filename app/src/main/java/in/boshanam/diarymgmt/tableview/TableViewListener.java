package in.boshanam.diarymgmt.tableview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.evrencoskun.tableview.sort.SortState;

import in.boshanam.diarymgmt.tableview.holder.CellViewHolder;
import in.boshanam.diarymgmt.tableview.holder.ColumnHeaderViewHolder;
import in.boshanam.diarymgmt.tableview.popup.CellLongPressPopup;


/**
 * Created by Siva on 2/25/2018.
 */

public class TableViewListener implements ITableViewListener {

    private final TableView tableView;
    private final TableViewHelper tableViewHelper;
    private final boolean enableDelete;

    public TableViewListener(TableView pTableView, TableViewHelper tableViewHelper, boolean enableDelete) {
        this.tableView = pTableView;
        this.tableViewHelper = tableViewHelper;
        this.enableDelete = enableDelete;
    }

    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder p_jCellView, int p_nXPosition, int
            p_nYPosition) {

    }

    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
        if (cellView != null && cellView instanceof CellViewHolder) {

            // Create Long Press Popup
            CellViewHolder cellViewHolder = (CellViewHolder) cellView;
            CellLongPressPopup popup = new CellLongPressPopup(
                    cellViewHolder, tableView, tableViewHelper, row, column, enableDelete);

            // Show
            popup.show();
        }
    }

    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder p_jColumnHeaderView, int p_nXPosition) {
        ColumnHeaderViewHolder p_jColumnHeaderView1 = (ColumnHeaderViewHolder) p_jColumnHeaderView;
        int mXPosition = p_jColumnHeaderView1.getAdapterPosition();
        SortState sortState = tableView.getSortingStatus(mXPosition);
        if (sortState == SortState.ASCENDING) {
            tableView.sortColumn(mXPosition, SortState.DESCENDING);
        } else if (sortState == SortState.DESCENDING) {
            tableView.sortColumn(mXPosition, SortState.ASCENDING);
        } else {
            tableView.sortColumn(mXPosition, SortState.ASCENDING);
        }
        tableView.remeasureColumnWidth(mXPosition);
    }

    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder p_jColumnHeaderView,
                                          int p_nXPosition) {
    }

    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder p_jRowHeaderView, int
            p_nYPosition) {

    }

    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder p_jRowHeaderView, int
            p_nYPosition) {

    }
}
