package in.boshanam.diarymgmt.util;

import java.util.Comparator;

/**
 * Created by Siva on 2/18/2018.
 */

public class StringComparator implements Comparator<String[]> {

    private final int colIndex;

    public StringComparator(int colIndex) {
        this.colIndex = colIndex;
    }

    @Override
    public int compare(String[] o1, String[] o2) {
        return StringUtils.compare(o1[colIndex], o2[colIndex]);
    }
}
