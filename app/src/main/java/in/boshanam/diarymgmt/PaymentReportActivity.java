package in.boshanam.diarymgmt;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.codecrafters.tableview.SortableTableView;
import in.boshanam.diarymgmt.app.constants.FarmerConstants;
import in.boshanam.diarymgmt.app.constants.PaymentConstants;
import in.boshanam.diarymgmt.domain.BaseAppCompatActivity;
import in.boshanam.diarymgmt.repository.FireBaseDao;
import in.boshanam.diarymgmt.util.ui.UIHelper;

public class PaymentReportActivity extends BaseAppCompatActivity {
    @BindView(R.id.payment_from_date)
    EditText fromDate;
    @BindView(R.id.payment_to_date)
    EditText toDate;
    @BindView(R.id.payment_retrive)
    Button retrive;
    private int year;
    private int month;
    private int dayOfMonth;
    private Calendar calendar;
    @BindView(R.id.paymentsListingTableView)
    SortableTableView<String[]> paymentsListingTableView;

    private String id;

    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_report);
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
            calendar.set(calendar.get(Calendar.YEAR), Calendar.MONTH -1, 16);
            fromDate.setText(dateFormatterDisplay.format(calendar.getTime()));

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            toDate.setText(dateFormatterDisplay.format(calendar.getTime()));
        }
        String dairyId = getDairyID();
        listenerRegistration = UIHelper.initGridWithQuerySnapshot(this, paymentsListingTableView,
                PaymentConstants.PaymentsDataGrid.class, FireBaseDao.buildAllFarmersQuery(dairyId), null, true);
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

    @OnClick(R.id.payment_from_date)
    public void setFromDate(View view) {

        DatePickerDialog dialog = new DatePickerDialog(PaymentReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                PaymentReportActivity.this.year = year;
                PaymentReportActivity.this.month = month;
                PaymentReportActivity.this.dayOfMonth = dayOfMonth;
                updateFromDateDisplay();
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @OnClick(R.id.payment_to_date)
    public void setToDate(View view) {

        DatePickerDialog dialog = new DatePickerDialog(PaymentReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                PaymentReportActivity.this.year = year;
                PaymentReportActivity.this.month = month;
                PaymentReportActivity.this.dayOfMonth = dayOfMonth;
                updateToDateDisplay();
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
