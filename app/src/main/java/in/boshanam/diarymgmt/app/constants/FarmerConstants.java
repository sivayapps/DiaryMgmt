package in.boshanam.diarymgmt.app.constants;

import in.boshanam.diarymgmt.R;
import in.boshanam.diarymgmt.domain.MilkType;

/**
 * Created by Siva on 2/9/2018.
 */
public interface FarmerConstants {
    int FARMER_ID_COL_HEADER_KEY = R.string.farmer_id_col_header_key;
    int FARMER_NAME_COL_HEADER_KEY = R.string.farmer_name_col_header_key;
    int FARMER_MILK_TYPE_COL_HEADER_KEY = AppConstants.MILK_TYPE_COL_HEADER_KEY;

    enum FarmerDataGrid implements GridBaseEnum {

        NO(FARMER_ID_COL_HEADER_KEY, "id", GridColumnType.NUMBER) {
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
        NAME(FARMER_NAME_COL_HEADER_KEY, "name", GridColumnType.STRING),
        MILK_TYPE(FARMER_MILK_TYPE_COL_HEADER_KEY, "milkType", GridColumnType.ENUM, new DisplayValueProvider(MilkType.class));

        private int columnHeader;
        private String fieldName;
        private GridColumnType columnType;
        private DisplayValueProvider displayValueProvider;

        FarmerDataGrid(int columnHeader, String fieldName, GridColumnType columnType) {
            this.columnHeader = columnHeader;
            this.fieldName = fieldName;
            this.columnType = columnType;
        }

        FarmerDataGrid(int columnHeader, String fieldName, GridColumnType columnType, DisplayValueProvider displayValueProvider) {
            this(columnHeader, fieldName, columnType);
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
            return null;
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
