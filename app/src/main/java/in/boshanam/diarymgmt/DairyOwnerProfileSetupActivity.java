package in.boshanam.diarymgmt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.boshanam.diarymgmt.command.ListenerAdapter;
import in.boshanam.diarymgmt.domain.DairyOwner;
import in.boshanam.diarymgmt.repository.FireBaseDao;
import in.boshanam.diarymgmt.util.StringUtils;

public class DairyOwnerProfileSetupActivity extends AppCompatActivity {

    private static final String MOBILE_PATTERN = "[0-9]{10}";

    @BindView(R.id.dairy_name)
    EditText dairyName;
    @BindView(R.id.owner_name)
    EditText ownerName;
    @BindView(R.id.phone_number)
    EditText phoneNumber;
    @BindView(R.id.save)
    Button save;


//    private String uid;
//    private String email;
//    private String dairyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy_owner_profilesetup);
        ButterKnife.bind(this);
        findViewById(R.id.dairy_owner_profile_loadingProgressPanel).setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FireBaseDao.findDairyOwner(this, user.getUid(), new ListenerAdapter<DairyOwner>() {
            @Override
            public void onSuccess(DairyOwner data) {
                if (data == null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String displayName = user.getEmail();
                    String phoneNumberVal = user.getPhoneNumber();
                    if (StringUtils.isNotBlank(displayName)) {
                        ownerName.setText(displayName);
                    }
                    if (StringUtils.isNotBlank(phoneNumberVal)) {
                        phoneNumber.setText(displayName);
                    }
                } else {
                    if (StringUtils.isNotBlank(data.getName())) {
                        ownerName.setText(data.getName());
                    }
                    if (StringUtils.isNotBlank(data.getPhone())) {
                        phoneNumber.setText(data.getPhone());
                    }
                    //TODO set remaining UI fields
                    //TODO MUST SET DAIRY_ID if not null in DairyOwner object from DB
                }
                findViewById(R.id.dairy_owner_profile_loadingProgressPanel).setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Exception data) {
                //TODO handle error politely
                findViewById(R.id.dairy_owner_profile_loadingProgressPanel).setVisibility(View.GONE);
            }
        });

    }

    @OnClick(R.id.save)
    public void save(View view) {
        if (validate()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            String email = user.getEmail();
            final DairyOwner dairyOwner = new DairyOwner();
            dairyOwner.setUid(uid);
            dairyOwner.setDairyName(dairyName.getText().toString());
            dairyOwner.setEmail(email);
            dairyOwner.setPhone(phoneNumber.getText().toString());
            dairyOwner.setName(ownerName.getText().toString());
            dairyOwner.setDairyId("");//TODO get dairyID from View and set here

            FireBaseDao.saveDairyOwner(this, dairyOwner, new ListenerAdapter() {
                @Override
                public void onSuccess(Object data) {
                    Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(DairyOwnerProfileSetupActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(@NonNull Exception e) {
                    //TODO handle error politely
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


