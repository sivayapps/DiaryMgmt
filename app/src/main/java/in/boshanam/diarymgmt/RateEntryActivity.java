package in.boshanam.diarymgmt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.boshanam.diarymgmt.domain.Rate;

public class RateEntryActivity extends AppCompatActivity {
    @BindView(R.id.rate_milk_type)
    Spinner milkType;
    @BindView(R.id.enter_fat)
    EditText fat;
    @BindView(R.id.rate)
    EditText rate;
    @BindView(R.id.rate_save)
    Button save;
    @BindView(R.id.finish)
    Button finish;
    private final String MILK_TYPE = "COW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_entry);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rate_save)
    public void save(View view) {
        if (validate()) {
            if (MILK_TYPE.equals(milkType.getSelectedItem().toString())) {
                final Rate rate = new Rate();
                rate.setMilkType(milkType.getSelectedItem().toString());
            } else {

            }
        }
    }

    @OnClick(R.id.finish)
    public void finish(View view) {
        startActivity(new Intent(this, MainMenuActivity.class));
    }

    private boolean validate() {
        if (fat.getText().toString().trim().length() == 0 || rate.getText().toString().trim().length() == 0) {

            Toast.makeText(getApplicationContext(), "pls fill the empty fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}

