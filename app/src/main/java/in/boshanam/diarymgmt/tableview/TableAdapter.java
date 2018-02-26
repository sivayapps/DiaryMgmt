package in.boshanam.diarymgmt.tableview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import in.boshanam.diarymgmt.R;
import in.boshanam.diarymgmt.app.constants.GridBaseEnum;
import in.boshanam.diarymgmt.app.constants.GridColumnType;
import in.boshanam.diarymgmt.tableview.holder.CellViewHolder;
import in.boshanam.diarymgmt.tableview.holder.ColumnHeaderViewHolder;
import in.boshanam.diarymgmt.tableview.holder.MoneyCellViewHolder;
import in.boshanam.diarymgmt.tableview.holder.RowHeaderViewHolder;
import in.boshanam.diarymgmt.tableview.model.CellModel;
import in.boshanam.diarymgmt.tableview.model.ColumnHeaderModel;
import in.boshanam.diarymgmt.tableview.model.RowHeaderModel;
import in.boshanam.diarymgmt.tableview.model.TableViewModelDef;


/**
 * Created by Siva on 2/25/2018.
 */
public class TableAdapter<T, E extends Enum<E> & GridBaseEnum> extends AbstractTableAdapter<ColumnHeaderModel, RowHeaderModel, CellModel> {

    private TableViewModelDef<T, E> tableViewModelDef;

    public TableAdapter(Context context, TableViewModelDef tableViewModelDef) {
        super(context);
        this.tableViewModelDef = tableViewModelDef;
    }

    @Override
    public RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout;
        E columnDef = this.tableViewModelDef.getColumnDef(viewType);
        GridColumnType colType = null;
        if (columnDef != null) {
            colType = columnDef.getColumnType();
        }
        if (colType == GridColumnType.MONEY) {
            // Get money cell xml Layout
            layout = LayoutInflater.from(mContext).inflate(R.layout.tableview_money_cell_layout, parent, false);
            // Create the relevant view holder
            return new MoneyCellViewHolder(layout);
        }

        // Get default Cell xml Layout
        layout = LayoutInflater.from(mContext).inflate(R.layout.tableview_cell_layout,
                parent, false);
        // Create a Cell ViewHolder
        return new CellViewHolder(mContext, layout, columnDef);
    }

    @Override
    public int getCellItemViewType(int position) {
        Enum columnDef = this.tableViewModelDef.getColumnDef(position);
        if (columnDef != null) {
            return columnDef.ordinal();
        }
        Log.i("TableAdapter", "ColumnDef not found");
        return 0;
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object p_jValue, int
            p_nXPosition, int p_nYPosition) {
        CellModel cell = (CellModel) p_jValue;

        if (holder instanceof CellViewHolder) {
            // Get the holder to update cell item text
            ((CellViewHolder) holder).setCellModel(cell, p_nXPosition);

        } else if (holder instanceof MoneyCellViewHolder) {
            ((MoneyCellViewHolder) holder).setCellModel(cell);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.tableview_column_header_layout, parent, false);
        return new ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int p_nXPosition) {
        ColumnHeaderModel columnHeader = (ColumnHeaderModel) p_jValue;

        // Get the holder to update cell item text
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;

        columnHeaderViewHolder.setColumnHeaderModel(columnHeader, p_nXPosition);
    }

    @Override
    public RecyclerView.ViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {

        // Get Row Header xml Layout
        View layout = LayoutInflater.from(mContext).inflate(R.layout.tableview_row_header_layout, parent, false);

        // Create a Row Header ViewHolder
        return new RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int p_nYPosition) {

        RowHeaderModel rowHeaderModel = (RowHeaderModel) p_jValue;

        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.row_header_textview.setText(rowHeaderModel.getData());

    }

    @Override
    public View onCreateCornerView() {
        return LayoutInflater.from(mContext).inflate(R.layout.tableview_corner_layout, null,
                false);
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }
}
