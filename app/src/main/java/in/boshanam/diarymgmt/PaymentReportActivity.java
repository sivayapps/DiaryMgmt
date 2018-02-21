package in.boshanam.diarymgmt;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.codecrafters.tableview.SortableTableView;
import in.boshanam.diarymgmt.app.constants.PaymentConstants;
import in.boshanam.diarymgmt.command.ListenerAdapter;
import in.boshanam.diarymgmt.domain.BaseAppCompatActivity;
import in.boshanam.diarymgmt.domain.CollectedMilk;
import in.boshanam.diarymgmt.domain.Farmer;
import in.boshanam.diarymgmt.repository.FireBaseDao;
import in.boshanam.diarymgmt.service.MilkRateCalculator;
import in.boshanam.diarymgmt.util.StringAsNumberComparator;
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

    private volatile boolean farmerListLoaded = false;
    private Map<String, Farmer> registeredFarmers = new HashMap<>();
    private MilkRateCalculator milkRateCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_report);
        ButterKnife.bind(this);
        String dairyId = getDairyID();
        registerFarmersCacheLoader(dairyId);
        initDateFormatters();
        calendar = Calendar.getInstance();
        int getDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (getDay > 15) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            fromDate.setText(dateFormatterDisplay.format(calendar.getTime()));
            calendar.set(Calendar.DAY_OF_MONTH, 15);
            toDate.setText(dateFormatterDisplay.format(calendar.getTime()));
        } else {
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, 16);
            fromDate.setText(dateFormatterDisplay.format(calendar.getTime()));
            int lastDate = calendar.getActualMaximum(Calendar.DATE);
            calendar.set(Calendar.DAY_OF_MONTH, lastDate);
            toDate.setText(dateFormatterDisplay.format(calendar.getTime()));
        }
    }

    protected void registerFarmersCacheLoader(String dairyId) {
        FireBaseDao.buildAllFarmersQuery(dairyId).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    Toast.makeText(PaymentReportActivity.this, "Failed loading Data-" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                Map<String, Farmer> idFarmerMap = new HashMap<>();
                for (DocumentSnapshot ds : documentSnapshots.getDocuments()) {
                    Farmer farmer = ds.toObject(Farmer.class);
                    idFarmerMap.put(farmer.getId(), farmer);
                }
                registeredFarmers = idFarmerMap;
                farmerListLoaded = true;
//                hideProgressBar(); TODO add progress
            }
        });
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

    @OnClick(R.id.payment_retrive)
    public void computeFarmerPayments() {
        String dateStr = fromDate.getText().toString();
        try {
            final Date fromDate = dateFormatterDisplay.parse(dateStr);
            dateStr = toDate.getText().toString();
            final Date toDate = dateFormatterDisplay.parse(dateStr);
            final String dairyId = getDairyID();
            MilkRateCalculator.build(this, dairyId, fromDate, toDate, new ListenerAdapter<MilkRateCalculator>() {
                @Override
                public void onSuccess(final MilkRateCalculator milkRateCalculator) {
                    PaymentReportActivity.this.milkRateCalculator = milkRateCalculator;
                    final Query collectedMilkQuery = FireBaseDao.buildCollectedMilkQuery(dairyId)
                            .whereGreaterThanOrEqualTo("date", fromDate)
                            .whereLessThanOrEqualTo("date", toDate);
                    collectedMilkQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot documentSnapshots) {
                            try {
                                Map<String, Float> farmerPaymentAmntMap = new HashMap<>();
                                for (DocumentSnapshot documentSnapshot : documentSnapshots.getDocuments()) {
                                    CollectedMilk collectedMilk = documentSnapshot.toObject(CollectedMilk.class);
                                    Float farmerPaymentAmnt = farmerPaymentAmntMap.get(collectedMilk.getFarmerId());
                                    if (farmerPaymentAmnt == null) {
                                        farmerPaymentAmnt = 0f;
                                    }
                                    if (collectedMilk.getMilkQuantity() > 0 && collectedMilk.getFat() > 0) {
                                        milkRateCalculator.computeAndSetMilkRate(collectedMilk, PaymentReportActivity.this);
                                    }
                                    farmerPaymentAmnt = farmerPaymentAmnt + collectedMilk.getMilkPriceComputed();
                                    farmerPaymentAmntMap.put(collectedMilk.getFarmerId(), farmerPaymentAmnt);
                                }
                                Set<String> farmerIds = new TreeSet<>(new StringAsNumberComparator());
                                farmerIds.addAll(farmerPaymentAmntMap.keySet());
                                farmerIds.addAll(registeredFarmers.keySet());

                                List<Object[]> rowsData = new ArrayList<>();
                                for (String farmerId : farmerIds) {
                                    Object[] row = new Object[3];
                                    row[0] = farmerId;
                                    Farmer farmer = registeredFarmers.get(farmerId);
                                    row[1] = farmer != null ? farmer.getName() : "";
                                    Float amount = farmerPaymentAmntMap.get(farmerId);
                                    row[2] = amount != null ? amount : 0.0f;
                                    rowsData.add(row);
                                }
                                UIHelper.initGridWithData(PaymentReportActivity.this, paymentsListingTableView,
                                        PaymentConstants.PaymentsDataGrid.class, rowsData, null, true);
                            } catch (Exception e) {
                                Log.e(TAG, "Exception while calculating farmer payments: ", e);
                                Toast.makeText(PaymentReportActivity.this, "Exception while calculating farmer payments"
                                        + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Exception while fetching Milk Collection data: ", e);
                            Toast.makeText(PaymentReportActivity.this, "Exception while fetching Milk Collection data: "
                                    + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Exception while fetching rates: ", e);
                    Toast.makeText(PaymentReportActivity.this, "Exception while fetching rates: "
                            + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Dateformat invalid", Toast.LENGTH_LONG).show();
        }
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
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
