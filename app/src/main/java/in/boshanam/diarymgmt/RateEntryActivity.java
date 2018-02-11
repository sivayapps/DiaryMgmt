package in.boshanam.diarymgmt;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnItemSelected;
import de.codecrafters.tableview.SortableTableView;
import in.boshanam.diarymgmt.app.constants.AppConstants;
import in.boshanam.diarymgmt.app.constants.RateConstants;
import in.boshanam.diarymgmt.command.ListenerAdapter;
import in.boshanam.diarymgmt.domain.Farmer;
import in.boshanam.diarymgmt.domain.MilkType;
import in.boshanam.diarymgmt.domain.Rate;
import in.boshanam.diarymgmt.repository.FireBaseDao;
import in.boshanam.diarymgmt.util.StringUtils;
import in.boshanam.diarymgmt.util.ui.UIHelper;

public class RateEntryActivity extends AppCompatActivity {
    public static final String TAG = "RateEntryActivity";
    public static final String DATE_FORMATE = "dd-MMM-yyyy";
    public static final String DB_DATE_FORMATE = "yyyyMMdd";
    @BindView(R.id.rate_milk_type)
    Spinner milkType;
    @BindView(R.id.enter_fat)
    EditText fat;
    @BindView(R.id.rate)
    EditText price;
    @BindView(R.id.rate_save)
    Button save;
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.effective_date)
    EditText effectiveDate;
    /*@BindView(R.id.rate_entry_table)
    TableLayout rateEntryTable;*/
    private Calendar calendar;
    private int year;
    private int month;
    private int dayOfMonth;

    @BindView(R.id.rateListingTableView)
    SortableTableView<String[]> rateListingTableView;

    private ListenerRegistration listenerRegistration;
    private DateFormat changeDateFormater;
    private DateFormat getFormater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_entry);
        ButterKnife.bind(this);
        calendar = Calendar.getInstance();
        changeDateFormater = new SimpleDateFormat(DATE_FORMATE, Locale.getDefault());
        effectiveDate.setText(changeDateFormater.format(calendar.getTime()));
        String dairyId = getDairyID();
        listenerRegistration = UIHelper.initGridWithQuerySnapshot(this, rateListingTableView,
                RateConstants.RateDataGrid.class, FireBaseDao.buildRateQuery(dairyId));
    }

    private String getDairyID() {
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.DAIRY_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(AppConstants.DAIRY_ID_KEY, null);
    }

    @OnClick(R.id.effective_date)
    public void setDate(View view) {

        DatePickerDialog dialog = new DatePickerDialog(RateEntryActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                RateEntryActivity.this.year = year;
                RateEntryActivity.this.month = month;
                RateEntryActivity.this.dayOfMonth = dayOfMonth;
                updateDateDisplay();
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    private void updateDateDisplay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        effectiveDate.setText(changeDateFormater.format(calendar.getTime()));
        effectiveDate.setInputType(InputType.TYPE_NULL);
    }

    @OnClick(R.id.rate_save)
    public void registerRates() {
        if (validate()) {
            saveRates();
        }
    }

    public void saveRates() {
        final Calendar calendar = Calendar.getInstance();
        getFormater = new SimpleDateFormat(DATE_FORMATE, Locale.getDefault());
        changeDateFormater = new SimpleDateFormat(DB_DATE_FORMATE);
        String dairyId = getDairyID();
        if (StringUtils.isBlank(dairyId)) {
            UIHelper.logoutAndShowLogin(this, "Dairy Information not available, logging out to reload");
            return;
        }
        String dateFormate = effectiveDate.getText().toString();
        MilkType getMilkType = null;
        final Rate rate = new Rate();
        rate.setDairyId(dairyId);
        rate.setActive(true);
        rate.setFat(Float.parseFloat(fat.getText().toString()));
        rate.setPrice(Float.parseFloat(price.getText().toString()));
        rate.setUpdateTime(calendar.getTime());
        if (milkType.getSelectedItemPosition() >= 0) {
            getMilkType = MilkType.getMilkTypeForIndex(milkType.getSelectedItemPosition());
            rate.setMilkType(getMilkType);
        }
        String expectedStringFormateDate = null;
        Date getDate = null;
        try {
            getDate = getFormater.parse(dateFormate);
            expectedStringFormateDate = changeDateFormater.format(getDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String rateId = expectedStringFormateDate + "-" + getMilkType;
        rate.setEffectiveDate(getDate);
        rate.setId(rateId);
        save(rate);
    }

    private void save(Rate rate) {

        findViewById(R.id.rate_loadingProgressPanel).setVisibility(View.VISIBLE);
        FireBaseDao.saveRates(this, rate, new ListenerAdapter<Rate>() {
            @Override
            public void onSuccess(Rate data) {
                Toast.makeText(getApplicationContext(), "Successfully Save", Toast.LENGTH_LONG).show();
                clearFormFields();
                findViewById(R.id.rate_loadingProgressPanel).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                findViewById(R.id.rate_loadingProgressPanel).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Failed to Save Rate, please retry.", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Failed to save Rate", e);
                //TODO handle error
            }
        });
    }

    private void clearFormFields() {
        price.setText("");
        fat.setText("");
    }


    @OnClick(R.id.finish)
    public void finish(View view) {
        startActivity(new Intent(this, MainMenuActivity.class));
    }

    private boolean validate() {
        if (fat.getText().toString().trim().length() == 0 || price.getText().toString().trim().length() == 0) {

            Toast.makeText(getApplicationContext(), "pls fill the empty fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }
}






