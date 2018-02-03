package in.boshanam.diarymgmt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import in.boshanam.diarymgmt.domain.DairyOwner;
import in.boshanam.diarymgmt.repository.FireBaseDao;

public class DairyOwnerProfileSetupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText dairyName;
    private EditText ownerName;
    private EditText phoneNumber;
    private String uid;
    private String email;
    private List<String> dairyIds;
    private String MobilePattern = "[0-9]{10}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy_owner_profilesetup);

        dairyName = (EditText) findViewById(R.id.dairy_name);
        ownerName = (EditText) findViewById(R.id.owner_name);
        phoneNumber = (EditText) findViewById(R.id.phone_number);

        findViewById(R.id.save).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (validate()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            uid = user.getUid();
            email = user.getEmail();

            dairyIds = new ArrayList<String>();
            dairyIds.add(dairyName.getText().toString());

            final DairyOwner dairyOwner = new DairyOwner();
            dairyOwner.setUid(uid);
            dairyOwner.setDairyIds(dairyIds);
            dairyOwner.setEmail(email);
            dairyOwner.setPhone(phoneNumber.getText().toString());
            dairyOwner.setName(ownerName.getText().toString());

            FireBaseDao.saveDairyOwner(dairyOwner, this, new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Successfully Save", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(DairyOwnerProfileSetupActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                }
            }, new Runnable() {
                @Override
                public void run() {
                    //TODO on failure
                }
            });
        }
    }


    private boolean validate() {
        String MobilePattern = "[0-9]{10}";
        if (dairyName.getText().length() > 25 || ownerName.getText().length() > 25) {
            Toast.makeText(this, "pls enter less the 25 character in dairy name or owner name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dairyName.getText().toString().trim().length() == 0 || ownerName.getText().toString().trim().length() == 0 || phoneNumber.length() == 0) {
            Toast.makeText(this, "pls fill the empty fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!phoneNumber.getText().toString().matches(MobilePattern)) {
            Toast.makeText(this, "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;
    }


}


