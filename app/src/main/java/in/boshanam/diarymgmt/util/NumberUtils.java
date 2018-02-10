package in.boshanam.diarymgmt.util;

/**
 * Created by ShivaKumar on 2/9/2018.
 */

public class NumberUtils {

    public  static boolean NaN(String str){
        if(!str.matches("[0-9]+")){
            return true;
        }
        return false;

    }
    public static boolean isNumeric(String str) {
        return str.matches("[+-]?\\d*(\\.\\d+)?");
    }

}
