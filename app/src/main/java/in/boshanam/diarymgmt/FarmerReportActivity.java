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
import android.widget.TextView;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import in.boshanam.diarymgmt.app.constants.FarmerPaymentReportsConstants;
import in.boshanam.diarymgmt.command.ListenerAdapter;
import in.boshanam.diarymgmt.domain.BaseAppCompatActivity;
import in.boshanam.diarymgmt.domain.CollectedMilk;
import in.boshanam.diarymgmt.domain.Farmer;
import in.boshanam.diarymgmt.repository.FireBaseDao;
import in.boshanam.diarymgmt.service.MilkRateCalculator;
import in.boshanam.diarymgmt.tableview.TableViewHelper;
import in.boshanam.diarymgmt.tableview.model.TableViewModelDef;

import in.boshanam.diarymgmt.util.StringUtils;


public class FarmerReportActivity extends BaseAppCompatActivity {

    @BindView(R.id.farmerPaymentFarmerId)
    EditText registerFarmerId;

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

    private int year;
    private int month;
    private int dayOfMonth;
    private Calendar calendar;

    @BindView(R.id.farmerPaymentsListingTableView)
    TableView farmerPaymentsListingTableView;

    private volatile boolean farmerListLoaded = false;
    private Map<String, Farmer> registeredFarmers = new HashMap<>();
    private MilkRateCalculator milkRateCalculator;
    private ListenerRegistration listenerRegistration;
    private Float farmerPaymentAmnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_reports);
        ButterKnife.bind(this);
        String dairyId = getDairyID();
        registerFarmersCacheLoader(dairyId);
        calendar = Calendar.getInstance();
        initDateFormatters();
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
                    Toast.makeText(FarmerReportActivity.this, "Failed loading Data-" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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

    @OnClick( R.id.farmer_payment_retrive)
    public void getFarmerReport() {
        if (validate()) {
            String farmerId = registerFarmerId.getText().toString();
            Farmer farmer = registeredFarmers.get(farmerId);
            String farmerNAME = farmer.getName();
            computeFarmerPayments(farmerId, farmerNAME);
        }
    }

    private void computeFarmerPayments(String getFarmerId, String farmerNAME) {
        String dateStr = fromDate.getText().toString();
        try {
            final Date fromDate = dateFormatterDisplay.parse(dateStr);
            dateStr = toDate.getText().toString();
            final Date toDate = dateFormatterDisplay.parse(dateStr);
            final String dairyId = getDairyID();
            MilkRateCalculator.build(this, dairyId, fromDate, toDate, new ListenerAdapter<MilkRateCalculator>() {
                @Override
                public void onSuccess(final MilkRateCalculator milkRateCalculator) {
                    FarmerReportActivity.this.milkRateCalculator = milkRateCalculator;
                    final Query collectedMilkQuery = FireBaseDao.buildCollectedMilkQuery(dairyId)
                            .whereEqualTo("farmerId", getFarmerId)
                            .whereGreaterThanOrEqualTo("date", fromDate)
                            .whereLessThanOrEqualTo("date", toDate);
                    collectedMilkQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot documentSnapshots) {
                            try {
                                Map<String, Float> farmerPaymentAmntMap = new HashMap<>();
                                for (DocumentSnapshot documentSnapshot : documentSnapshots.getDocuments()) {
                                    CollectedMilk collectedMilk = documentSnapshot.toObject(CollectedMilk.class);
                                    farmerPaymentAmnt = farmerPaymentAmntMap.get(collectedMilk.getFarmerId());
                                    if (farmerPaymentAmnt == null) {
                                        farmerPaymentAmnt = 0f;
                                    }
                                    if (collectedMilk.getMilkQuantity() > 0 && collectedMilk.getFat() > 0) {
                                        milkRateCalculator.computeAndSetMilkRate(collectedMilk, FarmerReportActivity.this);
                                    }

                                    farmerPaymentAmnt = farmerPaymentAmnt + collectedMilk.getMilkPriceComputed();
                                    farmerPaymentAmntMap.put(collectedMilk.getFarmerId(), farmerPaymentAmnt);
                                }

                                TableViewHelper tableViewHelper = TableViewHelper.buildTableViewHelper(FarmerReportActivity.this,
                                        farmerPaymentsListingTableView,
                                        new TableViewModelDef(FarmerPaymentReportsConstants.FarmerPaymentReportDataGrid.class), true);
                                listenerRegistration = tableViewHelper.initGridWithQuerySnapshot(FarmerReportActivity.this, collectedMilkQuery);
                                farmerId.setText(getFarmerId);
                                price.setText(String.format("%.2f", farmerPaymentAmnt));
                                farmerName.setText(farmerNAME);
                            } catch (Exception e) {
                                Log.e(TAG, "Exception while calculating farmer payments: ", e);
                                Toast.makeText(FarmerReportActivity.this, "Exception while calculating farmer payments"
                                        + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Exception while fetching Milk Collection data: ", e);
                            Toast.makeText(FarmerReportActivity.this, "Exception while fetching Milk Collection data: "
                                    + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Exception while fetching rates: ", e);
                    Toast.makeText(FarmerReportActivity.this, "Exception while fetching rates: "
                            + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Dateformat invalid", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validate() {
        String farmerID = registerFarmerId.getText().toString();
        if (StringUtils.isBlank(farmerID)) {
            Toast.makeText(FarmerReportActivity.this, "Farmer Id should not be blank", Toast.LENGTH_SHORT).show();
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
