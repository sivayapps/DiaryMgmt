package in.boshanam.diarymgmt.app.constants;

import in.boshanam.diarymgmt.R;
import in.boshanam.diarymgmt.domain.MilkType;
import in.boshanam.diarymgmt.domain.Shift;

/**
 * Created by Siva on 2/11/2018.
 */

public interface MilkCollectionConstants {

    int FARMER_ID_COL_HEADER_KEY = R.string.farmer_id_col_header_key;
    int MILK_COLLECTED_DATE_COL_HEADER_KEY = R.string.collected_milk_date_col_header_key;
    int MILK_COLLECTED_SHIFT_COL_HEADER_KEY = R.string.collected_milk_shift_col_header_key;
    int MILK_COLLECTED_SAMPLE_NUM_COL_HEADER_KEY = R.string.collected_milk_sample_num_col_header_key;
    int MILK_COLLECTED_QUANTITY_COL_HEADER_KEY = R.string.collected_milk_quantity_col_header_key;
    int MILK_COLLECTED_FAT_COL_HEADER_KEY = R.string.collected_milk_fat_col_header_key;
    int MILK_COLLECTED_PRICE_COL_HEADER_KEY = R.string.collected_milk_price_col_header_key;
    int MILK_COLLECTED_LTR_PRICE_COL_HEADER_KEY = R.string.collected_milk_ltr_price_col_header_key;


    enum CollectedMilkDataGrid implements GridBaseEnum {

        FARMER_ID(FARMER_ID_COL_HEADER_KEY, "farmerId", GridColumnType.NUMBER) {
            @Override
            public Object transformValue(Object rawValue) {
                if(rawValue != null) {
                    try {
                        Integer farmerId = Integer.parseInt(rawValue.toString());
                        return farmerId;
                    } catch (NumberFormatException e) {}
                }
                return rawValue;
            }
        },
        DATE(MILK_COLLECTED_DATE_COL_HEADER_KEY, "date", GridColumnType.DATE),
        SHIFT(MILK_COLLECTED_SHIFT_COL_HEADER_KEY, "shift", GridColumnType.ENUM, null, new DisplayValueProvider(Shift.class)),
//        MILK_TYPE(AppConstants.MILK_TYPE_COL_HEADER_KEY, "milkType", GridColumnType.ENUM, null, new DisplayValueProvider(MilkType.class)),
        SAMPLE_NUM(MILK_COLLECTED_SAMPLE_NUM_COL_HEADER_KEY, "milkSampleNumber", GridColumnType.NUMBER),
        QUANTITY(MILK_COLLECTED_QUANTITY_COL_HEADER_KEY, "milkQuantity", GridColumnType.NUMBER, "%.2f"),
        FAT(MILK_COLLECTED_FAT_COL_HEADER_KEY, "fat", GridColumnType.NUMBER, "%.1f"),
        LTR_PRICE(MILK_COLLECTED_LTR_PRICE_COL_HEADER_KEY, "perLtrPriceUsed", GridColumnType.MONEY, "%.2f"),
        PRICE(MILK_COLLECTED_PRICE_COL_HEADER_KEY, "milkPriceComputed", GridColumnType.MONEY, "%.2f");

        private DisplayValueProvider displayValueProvider;
        private int columnHeader;
        private String fieldName;
        private GridColumnType columnType;
        private String formatString;

        CollectedMilkDataGrid(int columnHeader, String fieldName, GridColumnType columnType) {
            this.columnHeader = columnHeader;
            this.fieldName = fieldName;
            this.columnType = columnType;
        }


        CollectedMilkDataGrid(int columnHeader, String fieldName, GridColumnType columnType, String formatString) {
            this(columnHeader, fieldName, columnType);
            this.formatString = formatString;
        }

        CollectedMilkDataGrid(int columnHeader, String fieldName, GridColumnType columnType, String formatString, DisplayValueProvider displayValueProvider) {
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
