package in.boshanam.diarymgmt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import in.boshanam.diarymgmt.domain.DairyOwner;

public class DairyOwnerProfileSetupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText dairyName;
    private EditText ownerName;
    private EditText phoneNumber;

    private String MobilePattern = "[0-9]{10}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.activity_dairy_owner_profilesetup);
        dairyName = findViewById(R.id.dairy_name);
        ownerName = findViewById(R.id.owner_name);
        phoneNumber = findViewById(R.id.phone_number);

        findViewById(R.id.save).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (validate()) {
            DairyOwner dairyOwner = new DairyOwner();
            //TODO extract fields dta and init entity
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }

    }

    private boolean validate() {
        String MobilePattern = "[0-9]{10}";
        if (dairyName.length() > 25 && ownerName.length() > 25) {

            Toast.makeText(getApplicationContext(), "pls enter less the 25 character in dairy name or owner name", Toast.LENGTH_SHORT).show();
            return false;

        } else if (dairyName.length() == 0 || ownerName.length() == 0 || phoneNumber.length() == 0) {

            Toast.makeText(getApplicationContext(), "pls fill the empty fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!phoneNumber.getText().toString().matches(MobilePattern)) {

            Toast.makeText(getApplicationContext(), "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}


