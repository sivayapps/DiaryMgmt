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

        NO(FARMER_ID_COL_HEADER_KEY, "id", GridColumnType.NUMBER, null),
        NAME(FARMER_NAME_COL_HEADER_KEY, "name", GridColumnType.STRING, null),
        AMOUNT(FARMER_AMOUNT_HEADER_KEY, "amount", GridColumnType.NUMBER, "%.2f");

        private int columnHeader;
        private String fieldName;
        private GridColumnType columnType;
        private String formatString;

        PaymentsDataGrid(int columnHeader, String fieldName, GridColumnType columnType, String formatString) {
            this.columnHeader = columnHeader;
            this.fieldName = fieldName;
            this.columnType = columnType;
            this.formatString = formatString;
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
            return -1;
        }
    }
}
