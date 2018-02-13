package in.boshanam.diarymgmt.app.constants;

import in.boshanam.diarymgmt.domain.ResourceMapped;

/**
 * Created by Siva on 2/13/2018.
 */

public class DisplayValueProvider<T extends Enum<T> & ResourceMapped> {
    private final Class<T> enumClass;

    public DisplayValueProvider(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    public int getResourceIdForValue(String value) {
        try {
            ResourceMapped mappedEnum = Enum.valueOf(enumClass, value);
            if (mappedEnum != null) {
                return mappedEnum.getResourceId();
            }
        } catch (Exception ignored) {
        }
        return -1;
    }
}
