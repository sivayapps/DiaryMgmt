package in.boshanam.diarymgmt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import de.codecrafters.tableview.SortableTableView;
import in.boshanam.diarymgmt.app.constants.MilkCollectionConstants;
import in.boshanam.diarymgmt.command.ListenerAdapter;
import in.boshanam.diarymgmt.domain.BaseAppCompatActivity;
import in.boshanam.diarymgmt.domain.CollectedMilk;
import in.boshanam.diarymgmt.domain.Farmer;
import in.boshanam.diarymgmt.domain.Shift;
import in.boshanam.diarymgmt.repository.FireBaseDao;
import in.boshanam.diarymgmt.util.StringUtils;
import in.boshanam.diarymgmt.util.ui.UIHelper;

public class FatEntryActivity extends BaseAppCompatActivity {

    @BindView(R.id.fat_entry_milk_collection_date)
    TextView fatDateView;

    @BindView(R.id.fat_entry_milk_collection_shift)
    TextView fatShiftView;

    @BindView(R.id.fat_entry_view_error_message_view)
    TextView errorMessageView;

    @BindView(R.id.fat_entry_milk_sample_id)
    EditText milkSampleIdField;

    @BindView(R.id.fat_entry_fat_field)
    EditText fatField;

    @BindView(R.id.fat_entry_lr_field)
    EditText lrField;

    @BindView(R.id.fat_entry_ltr_rate_entry_field)
    EditText ltrRateField;

    @BindView(R.id.fat_entry_view_collected_milk_listing_table_view)
    SortableTableView<String[]> collectedMilkListingTableView;

    @BindView(R.id.fat_save)
    Button saveFat;

    @BindView(R.id.fat_entry_farmer_details_display_text_field_id)
    TextView selectedFarmerDisplayView;

    private String dateForDisplay;
    private String dateForDbKey;
    private String shiftStr;
    private Shift shift;
    private Date collectionDate;

    private Map<String, Farmer> registeredFarmers;
    private Map<String, CollectedMilk> collectedMilkBySampleNum;
    private Map<String, CollectedMilk> collectedMilkByFarmerId;

    private volatile boolean farmerListLoaded = false;
    private volatile boolean collectedMilkDetailsLoaded = false;
    private boolean progressBarVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDateFormatters();
        setContentView(R.layout.activity_fat_entry);
        ButterKnife.bind(this);
//        findViewById(R.id.fat_entry_ltr_rate_entry_field_layout).setVisibility(View.GONE);
        Intent intent = getIntent();
        savedInstanceState = intent.getExtras();
        dateForDisplay = (String.valueOf(savedInstanceState.get("date")));
        shiftStr = (String.valueOf(savedInstanceState.get("shift")));
        shift = Shift.getShiftForIndex(savedInstanceState.getInt("shift_index", -1));
        fatDateView.setText(dateForDisplay);
        fatShiftView.setText(shiftStr);
        try {
            collectionDate = dateFormatterDisplay.parse(dateForDisplay);
            dateForDbKey = dateFormatterDbKey.format(collectionDate);
            Toast.makeText(this, "Selected date '" + dateForDisplay + "' invalid format", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Date parse error: " + e.getLocalizedMessage());
        }
        String dairyId = getDairyID();
        registerFarmersCacheLoader(dairyId);
        Query collectedMilkQuery = FireBaseDao.buildCollectedMilkQuery(dairyId)
                .whereEqualTo("shift", shift.name())
                .whereEqualTo("date", collectionDate);
        initMilkCollectionDetailsGrid(collectedMilkQuery);
        registerMilkCollectedCacheLoader(collectedMilkQuery);
    }


    private void registerMilkCollectedCacheLoader(Query collectedMilkQuery) {
        collectedMilkQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    Toast.makeText(FatEntryActivity.this, "Failed loading Data-" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                Map<String, CollectedMilk> collectedMilkByFarmerIdMap = new HashMap<>();
                Map<String, CollectedMilk> collectedMilkBySampleNumMap = new HashMap<>();
                for (DocumentSnapshot ds : documentSnapshots.getDocuments()) {
                    CollectedMilk collectedMilk = ds.toObject(CollectedMilk.class);
                    collectedMilkByFarmerIdMap.put(collectedMilk.getFarmerId(), collectedMilk);
                    collectedMilkBySampleNumMap.put("" + collectedMilk.getMilkSampleNumber(), collectedMilk);
                }
                collectedMilkByFarmerId = collectedMilkByFarmerIdMap;
                collectedMilkBySampleNum = collectedMilkBySampleNumMap;
                collectedMilkDetailsLoaded = true;
                hideProgressBar();
            }
        });
    }

    private void initMilkCollectionDetailsGrid(Query collectedMilkQuery) {
    /*    TableColumnWeightModel columnModel = new TableColumnWeightModel(MilkCollectionConstants.CollectedMilkDataGrid.values().length);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.FARMER_ID.ordinal(), 1);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.DATE.ordinal(), 2);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.SHIFT.ordinal(), 2);
//        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.MILK_TYPE.ordinal(), 2);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.SAMPLE_NUM.ordinal(), 2);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.QUANTITY.ordinal(), 1);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.FAT.ordinal(), 1);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.LTR_PRICE.ordinal(), 2);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.PRICE.ordinal(), 2);
        UIHelper.initGridWithQuerySnapshot(this, collectedMilkListingTableView,
                MilkCollectionConstants.CollectedMilkDataGrid.class, collectedMilkQuery, columnModel);*/
        UIHelper.initGridWithQuerySnapshot(this, collectedMilkListingTableView,
                MilkCollectionConstants.CollectedMilkDataGrid.class, collectedMilkQuery);
    }

    protected void registerFarmersCacheLoader(String dairyId) {
        FireBaseDao.buildAllFarmersQuery(dairyId).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    Toast.makeText(FatEntryActivity.this, "Failed loading Data-" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                Map<String, Farmer> idFarmerMap = new HashMap<>();
                for (DocumentSnapshot ds : documentSnapshots.getDocuments()) {
                    Farmer farmer = ds.toObject(Farmer.class);
                    idFarmerMap.put(farmer.getId(), farmer);
                }
                registeredFarmers = idFarmerMap;
                farmerListLoaded = true;
                hideProgressBar();
            }
        });
    }


    @OnFocusChange(R.id.fat_entry_lr_field)
    public void lrFieldOnFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            //we are interested only on moving out of field after entering input, ignore when focus received
            return;
        }
        validateLRField(null);
    }

    private boolean validateLRField(CollectedMilk collectedMilk) {
//        findViewById(R.id.fat_entry_ltr_rate_entry_field_layout).setVisibility(View.GONE);
        String lrStr = lrField.getText().toString().trim();
        if (StringUtils.isNotBlank(lrStr)) {
            float lrVal = 0;
            try {
                lrVal = Float.parseFloat(lrStr);
            } catch (Exception e) {
                errorMessageView.setText("Please provide valid LR value");
                errorMessageView.setVisibility(View.VISIBLE);
                return false;
            }
            if (lrVal > 0) {
                errorMessageView.setText("");
                errorMessageView.setVisibility(View.GONE);
//                findViewById(R.id.fat_entry_ltr_rate_entry_field_layout).setVisibility(View.VISIBLE);
                if (collectedMilk != null) {
                    collectedMilk.setLr(lrVal);
                }
                return true;
            }
            if (lrVal < 0) {
                errorMessageView.setText("Please provide valid LR value");
                errorMessageView.setVisibility(View.VISIBLE);
                return false;
            }
        }
        return true;
    }

    private boolean validateLtrRateField(CollectedMilk collectedMilk) {
        String valStr = ltrRateField.getText().toString().trim();
        if (StringUtils.isNotBlank(valStr)) {
            float val = 0;
            try {
                val = Float.parseFloat(valStr);
            } catch (Exception e) {
                errorMessageView.setText("Please provide valid Ltr Rate value");
                errorMessageView.setVisibility(View.VISIBLE);
                return false;
            }
            if (val < 0) {
                errorMessageView.setText("Please provide valid Ltr Rate value");
                errorMessageView.setVisibility(View.VISIBLE);
                return false;
            }
            if (val > 0) {
                errorMessageView.setText("");
                errorMessageView.setVisibility(View.GONE);
                if (collectedMilk != null) {
                    collectedMilk.setMilkPerLtrPriceOverride(val);
                }
                return true;
            }
        }
        return true;
    }

    private boolean validateFatField(CollectedMilk collectedMilk) {
        String valStr = fatField.getText().toString().trim();
        if (StringUtils.isNotBlank(valStr)) {
            float val = 0;
            try {
                val = Float.parseFloat(valStr);
            } catch (Exception e) {
                errorMessageView.setText("Please provide valid Fat value");
                errorMessageView.setVisibility(View.VISIBLE);
                return false;
            }
            if (val > 0) {
                errorMessageView.setText("");
                errorMessageView.setVisibility(View.GONE);
//                findViewById(R.id.fat_entry_ltr_rate_entry_field_layout).setVisibility(View.VISIBLE);
                if (collectedMilk != null) {
                    collectedMilk.setFat(val);
                }
                return true;
            } else {
                errorMessageView.setText("Please provide valid Fat value");
                errorMessageView.setVisibility(View.VISIBLE);
                return false;
            }
        }
        return false;
    }

    @OnFocusChange(R.id.fat_entry_milk_sample_id)
    public void sampleNumberFieldOnFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            //we are interested only on moving out of field after entering input, ignore when focus received
            return;
        }
        String farmerSampleNo = milkSampleIdField.getText().toString().trim();
        if (StringUtils.isNotBlank(farmerSampleNo)) {
            CollectedMilk collectedMilk = collectedMilkBySampleNum.get(farmerSampleNo);
            if (collectedMilk != null) {
                Farmer registeredFarmer = registeredFarmers.get(collectedMilk.getFarmerId());
                updateFarmerDetailsView(registeredFarmer, collectedMilk);
                updateCollectedMilkFields(collectedMilk);
            }
        } else {
            errorMessageView.setText("Please provide valid Sample Num");
            errorMessageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateCollectedMilkFields(CollectedMilk collectedMilk) {
        if (collectedMilk == null) {
            return;
        }
        float lr = collectedMilk.getLr();
        if (lr > 0) {
            lrField.setText(String.format("%.2f", lr));
        }
        float ltrRate = collectedMilk.getMilkPerLtrPriceOverride();
        if (ltrRate > 0) {
            ltrRateField.setText(String.format("%.2f", ltrRate));
        }
        float fat = collectedMilk.getFat();
        if (fat > 0) {
            fatField.setText(String.format("%.1f", fat));
        }
    }

    @OnClick(R.id.fat_save)
    public void saveFat() {
        String dateStr = fatDateView.getText().toString();
        String farmerSampleNo = milkSampleIdField.getText().toString().trim();
        if (StringUtils.isNotBlank(farmerSampleNo)) {
            CollectedMilk collectedMilk = collectedMilkBySampleNum.get(farmerSampleNo);
            if (collectedMilk != null) {
                try {
                    String collectionDateStr = dateFormatterDisplay.format(collectedMilk.getDate());
                    if (StringUtils.isBlank(collectionDateStr) || !StringUtils.equals(dateStr, collectionDateStr)) {
                        errorMessageView.setText("Selected date and sample no date doesn't match");
                        errorMessageView.setVisibility(View.VISIBLE);
                        return;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Unable to format Date in display format", e);
                    errorMessageView.setText("Invalid Date in stored data.");
                    errorMessageView.setVisibility(View.VISIBLE);
                    return;
                }
                if (!validateFatField(collectedMilk)) {
                    return;
                }
                String lrStr = lrField.getText().toString().trim();
                if (!validateLRField(collectedMilk) || (StringUtils.isNotBlank(lrStr) && !validateLtrRateField(collectedMilk))) {
                    return;
                }

                FireBaseDao.saveCollectedMilkDetails(this, collectedMilk, new ListenerAdapter() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(FatEntryActivity.this, "Successfully saved milk Fat details", Toast.LENGTH_SHORT).show();
                        resetFields();
                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Exception while saving milk Fat details", e);
                        Toast.makeText(FatEntryActivity.this, "Error while saving milk Fat details", Toast.LENGTH_LONG).show();
                    }

                });
            } else {
                errorMessageView.setText("Sample number not found");
                errorMessageView.setVisibility(View.VISIBLE);
                return;
            }
        } else {
            errorMessageView.setText("Sample number not provided");
            errorMessageView.setVisibility(View.GONE);
            return;
        }
    }

    private void hideProgressBar() {
        if (progressBarVisible && farmerListLoaded && collectedMilkDetailsLoaded) {
            findViewById(R.id.fat_entry_view_loading_progress_panel).setVisibility(View.GONE);
            progressBarVisible = false;
        }
    }

    private void updateFarmerDetailsView(Farmer registeredFarmer, CollectedMilk milk) {
        if (registeredFarmer == null) {
            return;
        }
        selectedFarmerDisplayView.setText(getString(R.string.farmer_id_heading) + " " + registeredFarmer.getId() + ", "
                + getString(R.string.farmer_name_heading) + " " + registeredFarmer.getName()
                + ", " + getString(R.string.quantity_header) + " " + String.format("%.2f", milk.getMilkQuantity()) + " "
                + getString(R.string.milk_unit));
        findViewById(R.id.fat_entry_farmer_details_display_text_field_id).setVisibility(View.VISIBLE);
    }

    private void resetFields() {
        milkSampleIdField.setText("");
        fatField.setText("");
        lrField.setText("");
        ltrRateField.setText("");

        errorMessageView.setText("");
        errorMessageView.setVisibility(View.GONE);

        selectedFarmerDisplayView.setText("");
        findViewById(R.id.fat_entry_farmer_details_display_text_field_id).setVisibility(View.GONE);
//        findViewById(R.id.fat_entry_ltr_rate_entry_field_layout).setVisibility(View.GONE);
    }
}
