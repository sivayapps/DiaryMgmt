package in.boshanam.diarymgmt.domain;

import in.boshanam.diarymgmt.R;

/**
 * Created by Siva on 2/11/2018.
 */

public enum Shift {
    MORNING(0), EVENING(1);

    private final int index;

    Shift(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static Shift getShiftForIndex(int index) {
        for (Shift shift : values()) {
            if (shift.index == index) {
                return shift;
            }
        }
        return null;
    }
}
