package in.boshanam.diarymgmt.app.constants;

import in.boshanam.diarymgmt.R;

/**
 * Created by ShivaKumar on 2/19/2018.
 */

public interface PaymentConstants {
    int FARMER_ID_COL_HEADER_KEY = R.string.farmer_id_col_header_key;
    int FARMER_NAME_COL_HEADER_KEY = R.string.farmer_name_col_header_key;
    int FARMER_AMOUNT_HEADER_KEY = R.string.payments_amount_col_header_key;

    enum PaymentsDataGrid implements GridBaseEnum {

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
        AMOUNT(FARMER_AMOUNT_HEADER_KEY, "amount", GridColumnType.MONEY);

        private int columnHeader;
        private String fieldName;
        private GridColumnType columnType;

        PaymentsDataGrid(int columnHeader, String fieldName, GridColumnType columnType) {
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
        public int getResourceIdForValue(String value) {
            return -1;
        }
    }
}
