package in.boshanam.diarymgmt;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectMilkActivity extends AppCompatActivity {

    @BindView(R.id.date)
     EditText date;
    private int year;
    private int month;
    private int dayOfMonth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_milk);
       ButterKnife.bind(this);
       /* date=(EditText)findViewById(R.id.date);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                DatePickerDialog dialog = new DatePickerDialog(CollectMilkActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        CollectMilkActivity.this.year=year;
                        CollectMilkActivity.this.month=month;
                        CollectMilkActivity.this.dayOfMonth=dayOfMonth;
                        updateDateDisplay();

                    }
                },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();

            }
        });*/

    }
   @OnClick(R.id.date)
    public void onClick(View  view) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(CollectMilkActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                CollectMilkActivity.this.year=year;
                CollectMilkActivity.this.month=month;
                CollectMilkActivity.this.dayOfMonth=dayOfMonth;
                updateDateDisplay();
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    private void updateDateDisplay() {
        date.setText(new StringBuffer().append(dayOfMonth).append("/").append(month + 1).append("/").append(year));
        date.setInputType(InputType.TYPE_NULL);
    }

}



    /*Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

*/




