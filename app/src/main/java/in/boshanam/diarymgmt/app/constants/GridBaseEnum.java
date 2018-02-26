package in.boshanam.diarymgmt.app.constants;

/**
 * Created by Siva on 2/9/2018.
 */

public interface GridBaseEnum {

    int getColumnHeader();
    String getFieldName();
    GridColumnType getColumnType();
    default String getFormatString() { return null;}
    int getResourceIdForValue(String value);
    default Object transformValue(Object rawValue) {
        return rawValue;
    }
}
