package in.boshanam.diarymgmt.util;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Siva on 2/18/2018.
 */

public class DateComparator implements Comparator<String[]> {

    private final int colIndex;
    private final SimpleDateFormat displayDateFormat;

    public DateComparator(int colIndex, String datePattern) {
        this.colIndex = colIndex;
        this.displayDateFormat = new SimpleDateFormat(datePattern, Locale.getDefault());
    }

    @Override
    public int compare(String[] o1, String[] o2) {
        String str1 = o1[colIndex];
        String str2 = o2[colIndex];
        if (str1 == str2) {
            return 0;
        }
        if (str1 == null) {
            return -1;
        }
        if (str2 == null) {
            return 1;
        }
        try {
            Date val1 = displayDateFormat.parse(str1);
            Date val2 = displayDateFormat.parse(str2);
            return val1.compareTo(val2);
        } catch (Exception e) {
            return StringUtils.compare(str1, str2);
        }
    }
}
