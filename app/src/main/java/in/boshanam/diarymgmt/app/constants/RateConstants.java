package in.boshanam.diarymgmt.app.constants;

import in.boshanam.diarymgmt.R;

import static in.boshanam.diarymgmt.app.constants.FarmerConstants.FARMER_ID_COL_HEADER_KEY;
import static in.boshanam.diarymgmt.app.constants.FarmerConstants.FARMER_MILK_TYPE_COL_HEADER_KEY;

/**
 * Created by Siva on 2/9/2018.
 */
public interface RateConstants {
    int RATE_DATE_COL_HEADER_KEY = R.string.rate_date_col_header_key;
    int RATE_MILK_TYPE_COL_HEADER_KEY = AppConstants.MILK_TYPE_COL_HEADER_KEY;
    int RATE_FAT_COL_HEADER_KEY = R.string.rate_fat_col_header_key;
    int RATE_PRICE_COL_HEADER_KEY = R.string.rate_price_col_header_key;


    enum RateDataGrid implements GridBaseEnum {

        FAT(RATE_FAT_COL_HEADER_KEY , "fat", GridColumnType.STRING),
        PRICE(RATE_PRICE_COL_HEADER_KEY, "price", GridColumnType.FLOAT);

        private int columnHeader;
        private String fieldName;
        private GridColumnType columnType;

        RateDataGrid(int columnHeader, String fieldName, GridColumnType columnType) {
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
    }
}
