package in.boshanam.diarymgmt;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.boshanam.diarymgmt.domain.Fat;
import in.boshanam.diarymgmt.domain.Milk;

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
        Date date = Calendar.getInstance().getTime();
    }

    @OnClick(R.id.fat_find)
    public void findFat() {
        if (findValidate()) {

        }
    }

    @OnClick(R.id.fat_save)
    public void saveFat() {
        if (saveValidate()) {
            String dateFormate = fatDateView.getText().toString();
            DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            try {
                Date date = formatter.parse(dateFormate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
