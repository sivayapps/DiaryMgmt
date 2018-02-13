package in.boshanam.diarymgmt.app.constants;

/**
 * Created by Siva on 2/9/2018.
 */

public interface GridBaseEnum {

    int getColumnHeader();
    String getFieldName();
    GridColumnType getColumnType();
    String getFormatString();
    int getResourceIdForValue(String value);
}
