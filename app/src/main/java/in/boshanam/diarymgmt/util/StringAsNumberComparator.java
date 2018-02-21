package in.boshanam.diarymgmt.util;

import java.util.Comparator;

/**
 * Created by Siva on 2/18/2018.
 */

public class StringAsNumberComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {
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
