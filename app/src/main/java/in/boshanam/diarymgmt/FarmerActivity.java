package in.boshanam.diarymgmt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.boshanam.diarymgmt.domain.DairyOwner;
import in.boshanam.diarymgmt.domain.Farmer;
import in.boshanam.diarymgmt.listeners.CustomOnItemSelectedListener;
import in.boshanam.diarymgmt.repository.FireBaseDao;

public class FarmerActivity extends AppCompatActivity {

    @BindView(R.id.generateId)
    EditText farmerId;
    @BindView(R.id.farmerName)
    EditText farmerName;
    @BindView(R.id.milkType)
    Spinner milkType;
    @BindView(R.id.register)
    Button register;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);
        ButterKnife.bind(this);
        Intent iin = getIntent();
        savedInstanceState = iin.getExtras();
        if (savedInstanceState != null) {
            id = (String.valueOf(savedInstanceState.get("generateId")));
            farmerId.setText(id);
        }
    }

    @OnClick(R.id.register)
    public void registerFarmer() {
        if (validate()) {
            final Farmer farmer = new Farmer();
            farmer.setId(farmerId.getText().toString());
            farmer.setName(farmerName.getText().toString());
            farmer.setMilkType(milkType.getSelectedItem().toString());
            FireBaseDao.saveFarmer(farmer, this, new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Successfully Save", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(FarmerActivity.this, MainMenuActivity.class);
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
        if (farmerName.getText().length() > 25) {
            Toast.makeText(this, "pls enter less the 25 character in farmer name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (farmerName.getText().toString().trim().length() == 0 || farmerId.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "pls fill the empty fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
