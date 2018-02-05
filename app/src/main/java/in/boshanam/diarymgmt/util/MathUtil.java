package in.boshanam.diarymgmt.util;

/**
 * Created by Siva on 2/5/2018.
 * from Commons MathUtils
 */

public class MathUtil {

    /**
     * We are okay with below precision for this project use case
     */
    public static final double EPS = 0.0000001;

    /**
     * Returns true iff both arguments are NaN or neither is NaN and they are
     * equal
     *
     * @param x first value
     * @param y second value
     * @return true if the values are equal or both are NaN
     */
    public static boolean equals(double x, double y) {
        return (Double.isNaN(x) && Double.isNaN(y)) || x == y;
    }

    /**
     * Returns true iff both arguments are equal or within the range of allowed
     * error (inclusive).
     * <p>
     * Two NaNs are considered equals, as are two infinities with same sign.
     * </p>
     *
     * @param x first value
     * @param y second value
     * @param eps the amount of absolute error to allow
     * @return true if the values are equal or within range of each other
     */
    public static boolean equals(double x, double y, double eps) {
        return equals(x, y) || (Math.abs(y - x) <= eps);
    }


    public static boolean equalsDefaultEPS(double x, double y) {
        return equals(x, y, EPS);
    }
}
