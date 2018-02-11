package in.boshanam.diarymgmt.domain;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import in.boshanam.diarymgmt.app.constants.AppConstants;

/**
 * Created by Siva on 2/11/2018.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {
    protected String getDairyID() {
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.DAIRY_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(AppConstants.DAIRY_ID_KEY, null);
    }
}
