package in.boshanam.diarymgmt.app.constants;

import in.boshanam.diarymgmt.R;
import in.boshanam.diarymgmt.domain.MilkType;

/**
 * Created by ShivaKumar on 2/19/2018.
 */

public interface PaymentConstants {
    int FARMER_ID_COL_HEADER_KEY = R.string.farmer_id_col_header_key;
    int FARMER_NAME_COL_HEADER_KEY = R.string.farmer_name_col_header_key;
    int FARMER_AMOUNT_HEADER_KEY = R.string.payments_amount_col_header_key;

    enum PaymentsDataGrid implements GridBaseEnum {

        NO(FARMER_ID_COL_HEADER_KEY, "id", GridColumnType.NUMBER),
        NAME(FARMER_NAME_COL_HEADER_KEY, "name", GridColumnType.STRING),
        AMOUNT(FARMER_AMOUNT_HEADER_KEY, "amount", GridColumnType.STRING);

        private int columnHeader;
        private String fieldName;
        private GridColumnType columnType;
        private DisplayValueProvider displayValueProvider;

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
