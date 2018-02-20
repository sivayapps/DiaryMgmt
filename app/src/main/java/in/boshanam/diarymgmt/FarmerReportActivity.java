package in.boshanam.diarymgmt;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.codecrafters.tableview.SortableTableView;
import in.boshanam.diarymgmt.app.constants.FarmerPaymentReportsConstants;
import in.boshanam.diarymgmt.domain.BaseAppCompatActivity;
import in.boshanam.diarymgmt.repository.FireBaseDao;
import in.boshanam.diarymgmt.util.StringUtils;
import in.boshanam.diarymgmt.util.ui.UIHelper;

public class FarmerReportActivity extends BaseAppCompatActivity {
    @BindView(R.id.farmerPaymentFarmerId)
    EditText getFarmerId;
    @BindView(R.id.farmer_payment_from_date)
    EditText fromDate;
    @BindView(R.id.farmer_payment_to_date)
    EditText toDate;
    @BindView(R.id.farmer_payment_retrive)
    Button retrive;
    @BindView(R.id.farmerPaymentsReportsFarmerId)
    TextView farmerId;
    @BindView(R.id.farmerPaymentsReportsFarmerName)
    TextView farmerName;
    @BindView(R.id.farmerPaymentsReportsPrice)
    TextView price;
    @BindView(R.id.farmerPaymentReport)
    Button search;
    private int year;
    private int month;
    private int dayOfMonth;
    private Calendar calendar;
    @BindView(R.id.farmerPaymentsListingTableView)
    SortableTableView<String[]> farmerPaymentsListingTableView;


    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_reports);
        ButterKnife.bind(this);
        calendar = Calendar.getInstance();
        initDateFormatters();
        int getDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (getDay > 15) {
            calendar.set(calendar.get(Calendar.YEAR), Calendar.MONTH - 1, 1);
            fromDate.setText(dateFormatterDisplay.format(calendar.getTime()));
            calendar.set(calendar.get(Calendar.YEAR), Calendar.MONTH - 1, 15);
            toDate.setText(dateFormatterDisplay.format(calendar.getTime()));
        } else {
            calendar.set(calendar.get(Calendar.YEAR), Calendar.MONTH - 1, 16);
            fromDate.setText(dateFormatterDisplay.format(calendar.getTime()));

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            toDate.setText(dateFormatterDisplay.format(calendar.getTime()));
        }
        String dairyId = getDairyID();
        listenerRegistration = UIHelper.initGridWithQuerySnapshot(this, farmerPaymentsListingTableView,
                FarmerPaymentReportsConstants.FarmerPaymentReportDataGrid.class, FireBaseDao.buildAllFarmersQuery(dairyId), null, true);
    }

    @OnClick(R.id.farmerPaymentReport)
    public void searchFarmer() {
        if (validate()) {
            search();
        }
    }

    private void search() {
        String dairyId = getDairyID();
        String farmerID = String.valueOf(getFarmerId.getText().toString());
        listenerRegistration = UIHelper.initGridWithQuerySnapshot(this, farmerPaymentsListingTableView,
                FarmerPaymentReportsConstants.FarmerPaymentReportDataGrid.class, FireBaseDao.buildAllFarmersQuery(dairyId), null, true);


    }

    private boolean validate() {
        String farmerID = farmerId.getText().toString();
        if (StringUtils.isBlank(farmerID)) {
            Toast.makeText(this, "Farmer Id should not be blank", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateFromDateDisplay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        fromDate.setText(dateFormatterDisplay.format(calendar.getTime()));
        fromDate.setInputType(InputType.TYPE_NULL);
    }

    private void updateToDateDisplay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        toDate.setText(dateFormatterDisplay.format(calendar.getTime()));
        toDate.setInputType(InputType.TYPE_NULL);
    }

    @OnClick(R.id.farmer_payment_from_date)
    public void setFromDate(View view) {

        DatePickerDialog dialog = new DatePickerDialog(FarmerReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                FarmerReportActivity.this.year = year;
                FarmerReportActivity.this.month = month;
                FarmerReportActivity.this.dayOfMonth = dayOfMonth;
                updateFromDateDisplay();
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @OnClick(R.id.farmer_payment_to_date)
    public void setToDate(View view) {

        DatePickerDialog dialog = new DatePickerDialog(FarmerReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                FarmerReportActivity.this.year = year;
                FarmerReportActivity.this.month = month;
                FarmerReportActivity.this.dayOfMonth = dayOfMonth;
                updateToDateDisplay();
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

}
