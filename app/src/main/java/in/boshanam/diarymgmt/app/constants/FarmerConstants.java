package in.boshanam.diarymgmt.app.constants;

import in.boshanam.diarymgmt.R;

/**
 * Created by Siva on 2/9/2018.
 */
public interface FarmerConstants {
    int FARMER_ID_COL_HEADER_KEY = R.string.farmer_id_col_header_key;
    int FARMER_NAME_COL_HEADER_KEY = R.string.farmer_name_col_header_key;
    int FARMER_MILK_TYPE_COL_HEADER_KEY = AppConstants.MILK_TYPE_COL_HEADER_KEY;

    enum FarmerDataGrid implements GridBaseEnum {

        NO(FARMER_ID_COL_HEADER_KEY, "id", GridColumnType.NUMBER),
        NAME(FARMER_NAME_COL_HEADER_KEY, "name", GridColumnType.STRING),
        MILK_TYPE(FARMER_MILK_TYPE_COL_HEADER_KEY, "milkType", GridColumnType.STRING);

        private int columnHeader;
        private String fieldName;
        private GridColumnType columnType;

        FarmerDataGrid(int columnHeader, String fieldName, GridColumnType columnType) {
            this.columnHeader = columnHeader;
            this.fieldName = fieldName;
            this.columnType = columnType;
        }

        public int getColumnHeader() {
            return columnHeader;
        }

        public String getFieldName() {
            return fieldName;
        }

        public GridColumnType getColumnType() {
            return columnType;
        }

        @Override
        public String getFormatString() {
            return null;
        }

    }
}
