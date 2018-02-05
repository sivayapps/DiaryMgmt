package in.boshanam.diarymgmt;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DateShiftSelectActivity extends AppCompatActivity {

    @BindView(R.id.date)
    EditText date;
    @BindView(R.id.collect_milk_select_shift)
    Spinner shift;
    @BindView(R.id.collect_milk_next)
    Button next;
    private int year;
    private int month;
    private int dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_shift_select);
        ButterKnife.bind(this);
    }

    private void updateDateDisplay() {
        date.setText(new StringBuffer().append(dayOfMonth).append("/").append(month + 1).append("/").append(year));
        date.setInputType(InputType.TYPE_NULL);
    }

    @OnClick(R.id.date)
    public void setDate(View view) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

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
    public void next() {
        Intent intent = new Intent(DateShiftSelectActivity.this, CollectMilkActivity.class);
        intent.putExtra("date", date.getText().toString());
        intent.putExtra("shift", shift.getSelectedItem().toString());
        startActivity(intent);

    }


}








