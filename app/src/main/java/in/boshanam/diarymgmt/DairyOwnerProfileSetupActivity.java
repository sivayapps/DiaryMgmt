package in.boshanam.diarymgmt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    public static final String TAG = "DairyOwnerProfile";

    @BindView(R.id.dairy_name)
    EditText dairyName;
    @BindView(R.id.owner_name)
    EditText ownerName;
    @BindView(R.id.phone_number)
    EditText phoneNumber;
    @BindView(R.id.save)
    Button save;

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
                    String displayName = user.getDisplayName();
                    String phoneNumberVal = user.getPhoneNumber();
                    if (StringUtils.isNotBlank(displayName)) {
                        ownerName.setText(displayName);
                    }
                    if (StringUtils.isNotBlank(phoneNumberVal)) {
                        phoneNumber.setText(phoneNumberVal);
                    }
                } else {
                    if (StringUtils.isNotBlank(data.getName())) {
                        ownerName.setText(data.getName());
                    }
                    if (StringUtils.isNotBlank(data.getPhone())) {
                        phoneNumber.setText(data.getPhone());
                    }
                    if (StringUtils.isNotBlank(data.getDairyName())) {
                        dairyName.setText(data.getDairyName());
                    }
                    //TODO set remaining UI fields
                    //TODO MUST SET DAIRY_ID if not null in DairyOwner object from DB
                }
                findViewById(R.id.dairy_owner_profile_loadingProgressPanel).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Exception e) {
                //TODO handle error politely
                Toast.makeText(DairyOwnerProfileSetupActivity.this, "Data Retrieval failed with " + e.getLocalizedMessage(), Toast.LENGTH_LONG);
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
                    Log.e(TAG, "get failed with ", e);
                    Toast.makeText(DairyOwnerProfileSetupActivity.this, "Data Retrieval failed with " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    private boolean validate() {
        if (StringUtils.isBlank(dairyName.getText().toString())
                || StringUtils.isBlank(ownerName.getText().toString())
                || StringUtils.isBlank(phoneNumber.getText().toString())) {
            Toast.makeText(this, R.string.user_input_blank_error_msg, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}


