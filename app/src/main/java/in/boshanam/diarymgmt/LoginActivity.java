package in.boshanam.diarymgmt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import in.boshanam.diarymgmt.command.ListenerAdapter;
import in.boshanam.diarymgmt.domain.DairyOwner;
import in.boshanam.diarymgmt.repository.FireBaseDao;
import in.boshanam.diarymgmt.util.AppConstants;
import in.boshanam.diarymgmt.util.StringUtils;

import static in.boshanam.diarymgmt.util.AppConstants.DAIRY_ID_KEY;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    public static final String TAG = "LoginActivity";


    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireBaseDao.getInstance();//inti firestore
        // Create and launch sign-in intent
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Toast.makeText(this, "Already signed in " + user.getDisplayName(), Toast.LENGTH_LONG).show();
            checkDairyOwnerProfileAndProceed();
        } else {
            loginOrRegister();
        }
        // setContentView(R.layout.activity_login);
    }

    private void loginOrRegister() {
        // No user is signed in
        Toast.makeText(this, "Not signed in, now signing-in ", Toast.LENGTH_LONG).show();
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.AppTheme)
                        .build(),
                RC_SIGN_IN);
    }

    private void logoutUser() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(LoginActivity.this, "User signed out", Toast.LENGTH_LONG).show();
                        return;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                checkDairyOwnerProfileAndProceed();
//                setContentView(R.layout.activity_login);
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
                Toast.makeText(this, "Sign in failed, check response for error code", Toast.LENGTH_LONG).show();
                return;


            }
        }
    }

    private void checkDairyOwnerProfileAndProceed() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Toast.makeText(this, "Successfully signed in", Toast.LENGTH_LONG).show();
        if (user != null) {
            // Access a Cloud Firestore instance from your Activity
            FireBaseDao.onDairyOwnerProfileStatusValidation(this, user, new ListenerAdapter<DocumentSnapshot>() {

                @Override
                public void onSuccess(DocumentSnapshot document) {
                    if (document != null && document.exists()) {
                        DairyOwner dairyOwner = document.toObject(DairyOwner.class);
                        if (StringUtils.isNotBlank(dairyOwner.getDairyName()) && StringUtils.isNotBlank(dairyOwner.getName())
                                && StringUtils.isNotBlank(dairyOwner.getDairyId())) {
                            Log.d("DEBUG", "Owner Profile Complete - DocumentSnapshot data: " + dairyOwner);
                            SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.DAIRY_PREFERENCES_FILE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                            sharedPreferencesEditor.putString(DAIRY_ID_KEY, dairyOwner.getDairyId());
                            sharedPreferencesEditor.commit();
                            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                            startActivity(intent);
                            return;
                        }
                        Log.d("DEBUG", "Owner Profile Not Complete - DocumentSnapshot data: " + dairyOwner);
                    } else {
                        Log.d("DEBUG", "Owner Profile not found");
                    }
                    Intent intent = new Intent(LoginActivity.this, DairyOwnerProfileSetupActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "get failed with ", e);
                    Toast.makeText(LoginActivity.this, "Data Retrieval failed with " + e.getLocalizedMessage(), Toast.LENGTH_LONG);
                }
            });
        } else {
            //User not available, maybe login/register failed or token expired
            loginOrRegister();
        }
    }

}
