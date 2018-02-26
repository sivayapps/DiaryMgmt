package in.boshanam.diarymgmt.tableview.holder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.boshanam.diarymgmt.R;
import in.boshanam.diarymgmt.app.constants.AppConstants;
import in.boshanam.diarymgmt.app.constants.GridBaseEnum;
import in.boshanam.diarymgmt.app.constants.GridColumnType;
import in.boshanam.diarymgmt.tableview.model.CellModel;
import in.boshanam.diarymgmt.util.StringUtils;

import static in.boshanam.diarymgmt.app.constants.GridColumnType.DATE;


/**
 * Created by Siva on 2/25/2018.
 */
public class CellViewHolder<E extends Enum<E> & GridBaseEnum> extends AbstractViewHolder {
    public final TextView cell_textview;
    public final LinearLayout cell_container;
    private final SimpleDateFormat displayDateFormat = new SimpleDateFormat(AppConstants.DISPLAY_GRID_DATE_FORMAT, Locale.getDefault());
    private final E tableColDef;
    private final Context context;

    public CellViewHolder(Context context, View itemView, E tableColDef) {
        super(itemView);
        cell_textview = itemView.findViewById(R.id.cell_data);
        cell_container = itemView.findViewById(R.id.cell_container);
        this.tableColDef = tableColDef;
        this.context = context;
    }

    public void setCellModel(CellModel p_jModel, int pColumnPosition) {

        // Change textView align by column
        cell_textview.setGravity(ColumnHeaderViewHolder.COLUMN_TEXT_ALIGNS[pColumnPosition] |
                Gravity.CENTER_VERTICAL);
        // Set text
        Object data = p_jModel.getData();

        String formattedValue = null;
        if (tableColDef.getColumnType() == DATE && data instanceof Date) {
            formattedValue = displayDateFormat.format(data);
        } else if (data != null && tableColDef.getColumnType() == GridColumnType.ENUM) {
            int resourceIdForValue = tableColDef.getResourceIdForValue(data.toString());
            if (resourceIdForValue != -1) {
                formattedValue = context.getString(resourceIdForValue);
            }
        } else if (StringUtils.isNotBlank(tableColDef.getFormatString())) {
            String formatString = tableColDef.getFormatString();
            if (data != null && data.toString().trim().length() > 0) {
                formattedValue = String.format(formatString, data);
            } else {
                formattedValue = "" + data;
            }
        }
        if (data != null && StringUtils.isBlank(formattedValue)) {
            formattedValue = String.valueOf(data);
        }
        if(data == null) {
            formattedValue = "";
        }
        cell_textview.setText(formattedValue);

        // It is necessary to remeasure itself.
        cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        cell_textview.requestLayout();
    }

    @Override
    public void setSelected(SelectionState p_nSelectionState) {
        super.setSelected(p_nSelectionState);

        if (p_nSelectionState == SelectionState.SELECTED) {
            cell_textview.setTextColor(ContextCompat.getColor(cell_textview.getContext(), R.color
                    .selected_text_color));
        } else {
            cell_textview.setTextColor(ContextCompat.getColor(cell_textview.getContext(), R.color
                    .unselected_text_color));
        }
    }
}
