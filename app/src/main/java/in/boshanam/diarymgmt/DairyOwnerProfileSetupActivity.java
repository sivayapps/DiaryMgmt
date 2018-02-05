package in.boshanam.diarymgmt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.boshanam.diarymgmt.domain.DairyOwner;
import in.boshanam.diarymgmt.repository.FireBaseDao;

public class DairyOwnerProfileSetupActivity extends AppCompatActivity {

    private static final String MOBILE_PATTERN = "[0-9]{10}";

    @BindView(R.id.dairyName)
    EditText dairyName;
    @BindView(R.id.ownerName)
    EditText ownerName;
    @BindView(R.id.phoneNumber)
    EditText phoneNumber;
    @BindView(R.id.save)
    Button save;
    private String uid;
    private String email;
    private List<String> dairyIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy_owner_profilesetup);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.save)
    public void save(View view) {
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

            FireBaseDao.saveDairyOwner(this, dairyOwner, new Runnable() {
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
        if (dairyName.getText().length() > 25 || ownerName.getText().length() > 25) {
            Toast.makeText(this, "pls enter less the 25 character in dairy name or owner name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dairyName.getText().toString().trim().length() == 0 || ownerName.getText().toString().trim().length() == 0 || phoneNumber.length() == 0) {
            Toast.makeText(this, "pls fill the empty fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!phoneNumber.getText().toString().matches(MOBILE_PATTERN)) {
            Toast.makeText(this, "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}


