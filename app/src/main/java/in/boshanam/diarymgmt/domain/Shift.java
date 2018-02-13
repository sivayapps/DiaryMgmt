package in.boshanam.diarymgmt.domain;

import in.boshanam.diarymgmt.R;
import in.boshanam.diarymgmt.app.constants.DisplayValueProvider;

/**
 * Created by Siva on 2/11/2018.
 */

public enum Shift implements ResourceMapped {
    MORNING(0, R.string.shift_type_morning), EVENING(1, R.string.shift_type_evening);

    private final int index;
    private final int resourceId;

    Shift(int index, int resourceId) {
        this.index = index;
        this.resourceId = resourceId;
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

//    public int getResourceIdForValue(String value) {
//        if (value != null) {
//            try {
//                return valueOf(value).resourceId;
//            } catch (IllegalArgumentException ignore) {
//            }
//        }
//        return -1;
//    }

    @Override
    public int getResourceId() {
        return resourceId;
    }
}
