package in.boshanam.diarymgmt.util.ui;

import android.app.Activity;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import in.boshanam.diarymgmt.LoginActivity;

/**
 * Created by Siva on 2/9/2018.
 */

public class UIHelper {

    public static void logoutAndShowLogin(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    public static void setSpinnerSelectedValue(Spinner spin, String value) {

        for (int i = 0; i < spin.getCount(); i++) {
            if (spin.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                spin.setSelection(i);
                break;
            }

        }
    }

    public static void setSpinnerSelection(Spinner spin, int index) {
        spin.setSelection(index);
    }
}
