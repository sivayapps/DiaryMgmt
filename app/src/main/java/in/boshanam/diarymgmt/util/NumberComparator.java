package in.boshanam.diarymgmt.util;

import java.util.Comparator;

/**
 * Created by Siva on 2/18/2018.
 */

public class NumberComparator implements Comparator<String[]> {

    private final int colIndex;

    public NumberComparator(int colIndex) {
        this.colIndex = colIndex;
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
            Float val1 = Float.parseFloat(str1);
            Float val2 = Float.parseFloat(str2);
            return val1.compareTo(val2);
        } catch (NumberFormatException e) {
            return StringUtils.compare(str1, str2);
        }
    }
}
