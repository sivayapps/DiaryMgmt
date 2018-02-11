package in.boshanam.diarymgmt.app.constants;

import in.boshanam.diarymgmt.R;

/**
 * Created by Siva on 2/11/2018.
 */

public interface MilkCollectionConstants {
    enum Shift {
        MORNING(0), EVENING(1);

        private final int index;

        Shift(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

}
