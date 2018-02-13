package in.boshanam.diarymgmt.app.constants;

import in.boshanam.diarymgmt.R;
import in.boshanam.diarymgmt.domain.MilkType;

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
        DATE(RATE_DATE_COL_HEADER_KEY, "effectiveDate", GridColumnType.DATE, null),
        MILK_TYPE(RATE_MILK_TYPE_COL_HEADER_KEY, "milkType", GridColumnType.ENUM, null, new DisplayValueProvider(MilkType.class)),
        FAT(RATE_FAT_COL_HEADER_KEY, "fat", GridColumnType.NUMBER, "%.1f"),
        PRICE(RATE_PRICE_COL_HEADER_KEY, "price", GridColumnType.NUMBER, "%.2f");

        private int columnHeader;
        private String fieldName;
        private GridColumnType columnType;
        private String formatString;
        private DisplayValueProvider displayValueProvider;

        RateDataGrid(int columnHeader, String fieldName, GridColumnType columnType, String formatString) {
            this.columnHeader = columnHeader;
            this.fieldName = fieldName;
            this.columnType = columnType;
            this.formatString = formatString;
        }

        RateDataGrid(int columnHeader, String fieldName, GridColumnType columnType, String formatString, DisplayValueProvider displayValueProvider) {
            this(columnHeader, fieldName, columnType, formatString);
            this.displayValueProvider = displayValueProvider;
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
            return formatString;
        }


        @Override
        public int getResourceIdForValue(String value) {
            if (displayValueProvider != null) {
                return displayValueProvider.getResourceIdForValue(value);
            }
            return -1;
        }
    }
}
