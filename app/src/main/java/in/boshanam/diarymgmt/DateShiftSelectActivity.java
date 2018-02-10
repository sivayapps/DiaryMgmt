package in.boshanam.diarymgmt;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DateShiftSelectActivity extends AppCompatActivity {
    public static final String DATE_FORMATE = "dd-MMM-yyy";
    public static final String TAG = "DateShiftSelectActivity";

    @BindView(R.id.date)
    EditText date;
    @BindView(R.id.collect_select_shift)
    Spinner shift;
    @BindView(R.id.collect_milk_next)
    Button next;
    private int year;
    private int month;
    private int dayOfMonth;
    private String collectType;
    private SimpleDateFormat simpleDateFormat;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_shift_select);
        ButterKnife.bind(this);
        Intent iin = getIntent();
        savedInstanceState = iin.getExtras();
        if (savedInstanceState != null) {
            collectType = (String.valueOf(savedInstanceState.get("milkType")));
            Log.w("Collect Milk Type", collectType);
        }
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat(DATE_FORMATE);
        date.setText(simpleDateFormat.format(calendar.getTime()));
        Log.d(TAG, "MilkType" + collectType);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        if (hours > 12) {
            shift.setSelection(1);
        } else {
            shift.setSelection(0);
        }
        Log.d("Collect Milk Type", collectType);
    }

    private void updateDateDisplay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        date.setText(simpleDateFormat.format(calendar.getTime()));
        date.setInputType(InputType.TYPE_NULL);
    }

    @OnClick(R.id.date)
    public void setDate(View view) {

        DatePickerDialog dialog = new DatePickerDialog(DateShiftSelectActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                DateShiftSelectActivity.this.year = year;
                DateShiftSelectActivity.this.month = month;
                DateShiftSelectActivity.this.dayOfMonth = dayOfMonth;
                updateDateDisplay();
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    @OnClick(R.id.collect_milk_next)
    public void nextButtonHandler() {
        Log.d(TAG, "MilkType: " + collectType);
        if ("milk".equals(collectType)) {
            Intent intent = new Intent(DateShiftSelectActivity.this, CollectMilkActivity.class);
            intent.putExtra("date", date.getText().toString());
            intent.putExtra("shift", shift.getSelectedItem().toString());
            startActivity(intent);
        } else {
            Intent intent = new Intent(DateShiftSelectActivity.this, FatEntryActivity.class);
            intent.putExtra("date", date.getText().toString());
            intent.putExtra("shift", shift.getSelectedItem().toString());
            startActivity(intent);
        }

    }
}








