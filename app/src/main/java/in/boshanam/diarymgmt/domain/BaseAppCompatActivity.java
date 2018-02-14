package in.boshanam.diarymgmt.domain;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import in.boshanam.diarymgmt.app.constants.AppConstants;

/**
 * Created by Siva on 2/11/2018.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    public final String TAG = getClass().getSimpleName();


    protected DateFormat dateFormatterDisplay;
    protected DateFormat dateFormatterDbKey;

    protected void initDateFormatters() {
        dateFormatterDisplay = new SimpleDateFormat(AppConstants.DISPLAY_DATE_FORMAT, Locale.getDefault());
        dateFormatterDbKey = new SimpleDateFormat(AppConstants.DB_KEY_DATE_FORMAT, Locale.getDefault());
    }

    protected String getDairyID() {
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.DAIRY_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(AppConstants.DAIRY_ID_KEY, null);
    }
}
