package in.boshanam.diarymgmt;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FatEntryActivity extends AppCompatActivity {
    @BindView(R.id.fat_milk_date)
    TextView fatDateView;
    @BindView(R.id.fat_milk_shift)
    TextView fatShiftView;
    @BindView(R.id.fat_sample_id)
    EditText fatSampleId;
    @BindView(R.id.fat)
    EditText fat;
    @BindView(R.id.fat_lr)
    EditText lr;
    @BindView(R.id.fat_snf)
    EditText snf;
    @BindView(R.id.ltr_rate)
    EditText ltrRate;
    @BindView((R.id.fat_find))
    Button findFat;
    @BindView(R.id.fat_save)
    Button saveFat;


    private String date;
    private String shift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fat_entry);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        savedInstanceState = intent.getExtras();
        date = String.valueOf((savedInstanceState.get("date")));
        shift = String.valueOf((savedInstanceState.get("shift")));
        fatDateView.setText(date);
        fatShiftView.setText(shift);
    }

    @OnClick(R.id.fat_find)
    public void findFat() {
        if (findValidate()) {

        }
    }

    @OnClick(R.id.fat_save)
    public void saveFat() {
        if (saveValidate()) {

        }
    }

    private boolean findValidate() {
        if (fatSampleId.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "pls fill the empty sample id", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean saveValidate() {
        if (fatSampleId.getText().toString().trim().length() == 0 || fat.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "pls fill the empty fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
