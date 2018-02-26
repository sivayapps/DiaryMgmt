package in.boshanam.diarymgmt.tableview.popup;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.evrencoskun.tableview.TableView;

import in.boshanam.diarymgmt.R;
import in.boshanam.diarymgmt.tableview.holder.CellViewHolder;
import in.boshanam.diarymgmt.tableview.TableViewHelper;

import static in.boshanam.diarymgmt.tableview.popup.CellLongPressPopup.MenuOption.DELETE_ROW;


/**
 * Created by Siva on 2/26/2018.
 */
public class CellLongPressPopup extends PopupMenu implements PopupMenu
        .OnMenuItemClickListener {
    private static final String TAG = CellLongPressPopup.class.getSimpleName();

    enum MenuOption {
        DELETE_ROW;
        static MenuOption getOptionForOrdinal(int ordinal) {
            for (MenuOption menuOption : values()) {
                if (menuOption.ordinal() == ordinal) {
                    return menuOption;
                }
            }
            return null;
        }
    }

    private final TableView tableView;
    private final Context context;
    private final int rowNum;
    private final int colNum;
    private final TableViewHelper tableViewHelper;
    private final boolean enableDeleteOption;

    public CellLongPressPopup(CellViewHolder cellViewHolder, TableView tableView,
                              TableViewHelper tableViewHelper,
                              int rowNum, int colNum, boolean enableDeleteOption) {
        super(cellViewHolder.itemView.getContext(), cellViewHolder.itemView);
        this.tableView = tableView;
        this.tableViewHelper = tableViewHelper;
        this.enableDeleteOption = enableDeleteOption;
        this.context = cellViewHolder.itemView.getContext();
        this.rowNum = rowNum;
        this.colNum = colNum;

        initialize();
    }

    private void initialize() {
        createMenuItem();
        changeMenuItemVisibility();
        this.setOnMenuItemClickListener(this);
    }

    private void createMenuItem() {
        int menuOrder = 0;
        if(enableDeleteOption) {
            this.getMenu().add(Menu.NONE, DELETE_ROW.ordinal(), menuOrder++, context.getString(R.string.table_menu_delete_row));
        }
        // add new one ...

    }

    private void changeMenuItemVisibility() {
        // Determine which one shouldn't be visible
       /* SortState sortState = tableView.getSortingStatus(mXPosition);
        if (sortState == SortState.UNSORTED) {
            // Show others
        } else if (sortState == SortState.DESCENDING) {
            // Hide DESCENDING menu item
            getMenu().getItem(1).setVisible(false);
        } else if (sortState == SortState.ASCENDING) {
            // Hide ASCENDING menu item
            getMenu().getItem(0).setVisible(false);
        }*/
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        // Note: item id is index of menu item..
        int itemId = menuItem.getItemId();
        MenuOption option = MenuOption.getOptionForOrdinal(itemId);
        switch (option) {
            case DELETE_ROW:
                tableViewHelper.deleteRow(rowNum);
                break;
        }

        // Recalculate of the width values of the columns
//        tableView.remeasureColumnWidth(mXPosition);
        return true;
    }

}
