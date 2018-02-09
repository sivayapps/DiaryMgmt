package in.boshanam.diarymgmt.app.constants;

import in.boshanam.diarymgmt.R;

/**
 * Created by Siva on 2/9/2018.
 */
public interface FarmerConstants {
    int FARMER_ID_COL_HEADER_KEY = R.string.farmer_id_col_header_key;
    int FARMER_NAME_COL_HEADER_KEY = R.string.farmer_name_col_header_key;
    int FARMER_MILK_TYPE_COL_HEADER_KEY = AppConstants.MILK_TYPE_COL_HEADER_KEY;

    enum FarmerDataGrid {

        NO(FARMER_ID_COL_HEADER_KEY, "id"), NAME(FARMER_NAME_COL_HEADER_KEY, "name"), MILK_TYPE(FARMER_MILK_TYPE_COL_HEADER_KEY, "milkType");

        private int columnHeader;
        private String fieldName;

        FarmerDataGrid(int columnHeader, String fieldName) {
            this.columnHeader = columnHeader;
            this.fieldName = fieldName;
        }

        public int getColumnHeader() {
            return columnHeader;
        }

        public String getFieldName() {
            return fieldName;
        }

        public static int[] getHeaders() {
            int[] headers = new int[values().length];
            for (FarmerDataGrid farmerDataGrid : values()) {
                headers[farmerDataGrid.ordinal()] = farmerDataGrid.getColumnHeader();
            }
            return headers;
        }
    }
}
