package in.boshanam.diarymgmt.domain;

/**
 * Created by Siva on 2/9/2018.
 */

public enum MilkType {
    BUFFALO(0), COW(1);

    private final int index;

    MilkType(int index) {
        this.index = index;
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
}
