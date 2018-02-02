package in.boshanam.diarymgmt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class DairyOwnerProfileSetup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy_owner_profile_setup);
    }

    public void saveDairyOwnerProfile(View view) {
        Toast.makeText(this, "Dairy Owner Profile Saved...", Toast.LENGTH_LONG).show();
    }
}
