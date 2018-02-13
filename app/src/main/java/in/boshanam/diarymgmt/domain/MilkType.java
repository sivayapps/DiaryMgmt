package in.boshanam.diarymgmt.domain;

import in.boshanam.diarymgmt.R;
import in.boshanam.diarymgmt.app.constants.DisplayValueProvider;

/**
 * Created by Siva on 2/9/2018.
 */

public enum MilkType implements ResourceMapped {
    BUFFALO(0, R.string.milk_type_buffalo), COW(1, R.string.milk_type_cow);

    private final int index;
    private final int resourceId;

    MilkType(int index, int resourceId) {
        this.index = index;
        this.resourceId = resourceId;
    }

    public int getIndex() {
        return index;
    }

    public static MilkType getMilkTypeForIndex(int index) {
        for (MilkType milkType : values()) {
            if (milkType.index == index) {
                return milkType;
            }
        }
        return null;
    }

    public int getResourceId() {
        return resourceId;
    }
}
