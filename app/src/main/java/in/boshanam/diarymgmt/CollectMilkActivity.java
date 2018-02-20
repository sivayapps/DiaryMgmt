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

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import in.boshanam.diarymgmt.app.constants.MilkCollectionConstants;
import in.boshanam.diarymgmt.command.ListenerAdapter;
import in.boshanam.diarymgmt.domain.BaseAppCompatActivity;
import in.boshanam.diarymgmt.domain.CollectedMilk;
import in.boshanam.diarymgmt.domain.Farmer;
import in.boshanam.diarymgmt.domain.Shift;
import in.boshanam.diarymgmt.repository.FireBaseDao;
import in.boshanam.diarymgmt.service.MilkRateCalculator;
import in.boshanam.diarymgmt.util.MathUtil;
import in.boshanam.diarymgmt.util.StringUtils;
import in.boshanam.diarymgmt.util.ui.UIHelper;

public class CollectMilkActivity extends BaseAppCompatActivity {

    @BindView(R.id.collect_milk_date)
    TextView dateView;

    @BindView(R.id.collect_milk_shift)
    TextView shiftView;

    @BindView(R.id.fat_rates_missing_error_message_view)
    TextView errorMessageView;

    @BindView(R.id.registered_farmer_id)
    EditText registeredFarmerId;

    @BindView(R.id.farmer_milk_sample_id)
    EditText farmerSampleId;

    @BindView(R.id.collect_milk_quantity_field_id)
    EditText milkQuantityField;

    @BindView(R.id.collect_milk_fat_field_id)
    EditText fatField;

    @BindView(R.id.collect_milk_farmer_details_display_text_field_id)
    TextView selectedFarmerDisplayView;

    @BindView(R.id.collected_milk_listing_table_view)
    SortableTableView<String[]> collectedMilkListingTableView;

//    @BindView(R.id.find_farmer_button_id)
//    Button findFarmerMilk;

    @BindView(R.id.collect_milk_save)
    Button saveMilk;

    private String dateForDisplay;
    private String dateForDbKey;
    private String shiftStr;
    private Shift shift;
    private Date collectionDate;

    private volatile boolean farmerListLoaded = false;
    private volatile boolean collectedMilkDetailsLoaded = false;
    private boolean progressBarVisible = true;

    private Map<String, Farmer> registeredFarmers = new HashMap<>();
    private Map<String, CollectedMilk> collectedMilkBySampleNum = new HashMap<>();
    private Map<String, CollectedMilk> collectedMilkByFarmerId = new HashMap<>();
    private volatile MilkRateCalculator milkRateCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDateFormatters();
        setContentView(R.layout.activity_collect_milk);
        ButterKnife.bind(this);
        errorMessageView.setVisibility(View.GONE);
        showProgressBar();

        Intent iin = getIntent();
        savedInstanceState = iin.getExtras();
        dateForDisplay = (String.valueOf(savedInstanceState.get("date")));
        shiftStr = (String.valueOf(savedInstanceState.get("shift")));
        shift = Shift.getShiftForIndex(savedInstanceState.getInt("shift_index", -1));
        dateView.setText(dateForDisplay);
        shiftView.setText(shiftStr);
        try {
            collectionDate = dateFormatterDisplay.parse(dateForDisplay);
            dateForDbKey = dateFormatterDbKey.format(collectionDate);
        } catch (ParseException e) {
            Toast.makeText(this, "Selected date '" + dateForDisplay + "' invalid format", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Date parse error: " + e.getLocalizedMessage());
        }
        String dairyId = getDairyID();
        registerFarmersCacheLoader(dairyId);
        final Query collectedMilkQuery = FireBaseDao.buildCollectedMilkQuery(dairyId)
                .whereEqualTo("shift", shift.name())
                .whereEqualTo("date", collectionDate);
        MilkRateCalculator.build(this, dairyId, collectionDate, collectionDate, new ListenerAdapter<MilkRateCalculator>() {
            @Override
            public void onSuccess(MilkRateCalculator milkRateCalculator) {
                CollectMilkActivity.this.milkRateCalculator = milkRateCalculator;
                initMilkCollectionDetailsGrid(collectedMilkQuery);
                registerMilkCollectedCacheLoader(collectedMilkQuery);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Exception while fetching rates: ", e);
                Toast.makeText(CollectMilkActivity.this, "Exception while fetching rates: "
                        + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
//        FireBaseDao.buildRateQuery(dairyId, )
    }

    private void registerMilkCollectedCacheLoader(Query collectedMilkQuery) {
        collectedMilkQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    Toast.makeText(CollectMilkActivity.this, "Failed loading Data-" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                Map<String, CollectedMilk> collectedMilkByFarmerIdMap = new HashMap<>();
                Map<String, CollectedMilk> collectedMilkBySampleNumMap = new HashMap<>();
                for (DocumentSnapshot ds : documentSnapshots.getDocuments()) {
                    CollectedMilk collectedMilk = ds.toObject(CollectedMilk.class);
                    float priceUsed = collectedMilk.getPerLtrPriceUsed();
                    milkRateCalculator.computeAndSetMilkRate(collectedMilk, CollectMilkActivity.this);
                    if(!MathUtil.equalsDefaultEPS(priceUsed, collectedMilk.getPerLtrPriceUsed())) {
                        FireBaseDao.saveCollectedMilkDetails(CollectMilkActivity.this, collectedMilk, new ListenerAdapter() {
                        });
                    }
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
        TableColumnWeightModel columnModel = new TableColumnWeightModel(MilkCollectionConstants.CollectedMilkDataGrid.values().length);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.FARMER_ID.ordinal(), 1);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.DATE.ordinal(), 2);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.SHIFT.ordinal(), 2);
//        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.MILK_TYPE.ordinal(), 2);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.SAMPLE_NUM.ordinal(), 2);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.QUANTITY.ordinal(), 1);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.FAT.ordinal(), 1);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.LTR_PRICE.ordinal(), 2);
        columnModel.setColumnWeight(MilkCollectionConstants.CollectedMilkDataGrid.PRICE.ordinal(), 2);
//        UIHelper.initGridWithQuerySnapshot(this, collectedMilkListingTableView,
//                MilkCollectionConstants.CollectedMilkDataGrid.class, collectedMilkQuery, columnModel);
        UIHelper.initGridWithQuerySnapshot(this, collectedMilkListingTableView,
                MilkCollectionConstants.CollectedMilkDataGrid.class, collectedMilkQuery);
    }

    protected void registerFarmersCacheLoader(String dairyId) {
        FireBaseDao.buildAllFarmersQuery(dairyId).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    Toast.makeText(CollectMilkActivity.this, "Failed loading Data-" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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

    private void showProgressBar() {
        progressBarVisible = true;
        findViewById(R.id.find_farmer_milk_collection_loadingProgressPanel).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (progressBarVisible && farmerListLoaded && collectedMilkDetailsLoaded) {
            findViewById(R.id.find_farmer_milk_collection_loadingProgressPanel).setVisibility(View.GONE);
            progressBarVisible = false;
        }
    }

    @OnFocusChange(R.id.registered_farmer_id)
    public void farmerIdFieldOnFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            //we are interested only on moving out of field after entering input, ignore when focus received
            return;
        }
        String farmerID = registeredFarmerId.getText().toString().trim();
        String dairyId = getDairyID();
        if (StringUtils.isBlank(dairyId)) {
            UIHelper.logoutAndShowLogin(this, "Dairy Information not available, logging out to reload");
            return;
        }
        if (StringUtils.isNotBlank(farmerID)) {
            Farmer registeredFarmer = registeredFarmers.get(farmerID);
            updateFarmerDetailsView(registeredFarmer);
            CollectedMilk collectedMilk = collectedMilkByFarmerId.get(farmerID);
            if (collectedMilk == null) {
                collectedMilk = new CollectedMilk();
                collectedMilk.setFarmerId(farmerID);
            }
            updateCollectedMilkFields(collectedMilk, true);
        }
    }

    @OnFocusChange(R.id.farmer_milk_sample_id)
    public void sampleNumberFieldOnFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            //we are interested only on moving out of field after entering input, ignore when focus received
            return;
        }
        String farmerSampleNo = farmerSampleId.getText().toString().trim();
        if (StringUtils.isNotBlank(farmerSampleNo)) {
            CollectedMilk collectedMilk = collectedMilkBySampleNum.get(farmerSampleNo);
            if (collectedMilk != null) {
                Farmer registeredFarmer = registeredFarmers.get(collectedMilk.getFarmerId());
                updateFarmerDetailsView(registeredFarmer);
                updateCollectedMilkFields(collectedMilk, false);
            }
        }
    }

    private void updateCollectedMilkFields(CollectedMilk collectedMilk, boolean byFarmerId) {
        if (collectedMilk == null) {
            return;
        }
        int milkSampleNumber = collectedMilk.getMilkSampleNumber();
        if (byFarmerId && milkSampleNumber > 0) {
            farmerSampleId.setText("" + milkSampleNumber);
        } else if (byFarmerId) {
            farmerSampleId.setText("");
        } else {
            registeredFarmerId.setText(collectedMilk.getFarmerId());
        }
        float quantity = collectedMilk.getMilkQuantity();
        if (quantity > 0) {
            milkQuantityField.setText(String.format("%.2f", quantity));
        } else {
            milkQuantityField.setText("");
        }
        float fat = collectedMilk.getFat();
        if (fat > 0) {
            fatField.setText(String.format("%.1f", fat));
        } else {
            fatField.setText("");
        }
    }

    private void updateFarmerDetailsView(Farmer registeredFarmer) {
        if (registeredFarmer == null) {
            return;
        }
        selectedFarmerDisplayView.setText(getString(R.string.farmer_id_heading) + registeredFarmer.getId() + ", " + getString(R.string.farmer_name_heading) + registeredFarmer.getName());
        findViewById(R.id.collect_milk_farmer_details_display_view_id).setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.collect_milk_save)
    public void saveMilk() {
        CollectedMilk collectedMilk = extractCollectedMilkData();
        if (validateInputData(collectedMilk)) {
            if (collectedMilk.isValid()) {
                //Update cache to avoid missing snapshot events on save to auto refresh cache
                collectedMilkByFarmerId.put(collectedMilk.getFarmerId(), collectedMilk);
                collectedMilkBySampleNum.put("" + collectedMilk.getMilkSampleNumber(), collectedMilk);

                errorMessageView.setText("");
                errorMessageView.setVisibility(View.GONE);

                milkRateCalculator.computeAndSetMilkRate(collectedMilk, CollectMilkActivity.this);
                FireBaseDao.saveCollectedMilkDetails(this, collectedMilk, new ListenerAdapter() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(CollectMilkActivity.this, "Successfully saved collected milk details", Toast.LENGTH_SHORT).show();
                        resetFields();
                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Exception while saving Collected milk details", e);
                        Toast.makeText(CollectMilkActivity.this, "Error while saving Collected milk details", Toast.LENGTH_LONG).show();
                    }

                });
            } else {
                errorMessageView.setText(R.string.error_msg_verify_all_fields);
                errorMessageView.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean validateInputData(CollectedMilk collectedMilk) {
        if (!registeredFarmers.containsKey(collectedMilk.getFarmerId())) {
            errorMessageView.setText(R.string.error_msg_invalid_farmer_id);
            errorMessageView.setVisibility(View.VISIBLE);
            return false;
        }
        CollectedMilk collectedMilkExisting = collectedMilkBySampleNum.get("" + collectedMilk.getMilkSampleNumber());
        if (collectedMilkExisting != null && !collectedMilkExisting.getFarmerId().equals(collectedMilk.getFarmerId())) {
            errorMessageView.setText(R.string.error_msg_sample_number_already_used_for_other_farmer);
            errorMessageView.setVisibility(View.VISIBLE);
            return false;
        }
        //TODO validate sample number is not duplicated
        //Farmer number is valid and exists by checking with cache
        errorMessageView.setText("");
        errorMessageView.setVisibility(View.GONE);
        return true;
    }

    private CollectedMilk extractCollectedMilkData() {
        CollectedMilk collectedMilk = new CollectedMilk();
        String farmerID = registeredFarmerId.getText().toString().trim();
        String dairyId = getDairyID();
        if (StringUtils.isNotBlank(dateForDbKey) && shift != null && StringUtils.isNotBlank(farmerID)) {
            collectedMilk.setId(farmerID + "_" + dateForDbKey +  "_" + shift);
        }
        collectedMilk.setDairyId(dairyId);
        collectedMilk.setFarmerId(farmerID);
        collectedMilk.setDate(collectionDate);
        collectedMilk.setShift(this.shift);
        Farmer registeredFarmer = registeredFarmers.get(farmerID);
        if (registeredFarmer != null) {
            collectedMilk.setMilkType(registeredFarmer.getMilkType());
        }
        String sampleNumber = farmerSampleId.getText().toString().trim();
        if (StringUtils.isNotBlank(sampleNumber)) {
            try {
                collectedMilk.setMilkSampleNumber(Integer.parseInt(sampleNumber));
            } catch (NumberFormatException ignored) {
            }
        }
        String quantity = milkQuantityField.getText().toString().trim();
        if (StringUtils.isNotBlank(quantity)) {
            try {
                collectedMilk.setMilkQuantity(Float.parseFloat(quantity));
            } catch (NumberFormatException ignored) {
            }
        }
        String fat = fatField.getText().toString().trim();
        if (StringUtils.isNotBlank(fat)) {
            try {
                collectedMilk.setFat(Float.parseFloat(fat));
            } catch (NumberFormatException ignored) {
            }
        }
        return collectedMilk;
    }

    private void resetFields() {
        registeredFarmerId.setText("");
        farmerSampleId.setText("");
        milkQuantityField.setText("");
        fatField.setText("");
        selectedFarmerDisplayView.setText("");
        findViewById(R.id.collect_milk_farmer_details_display_view_id).setVisibility(View.GONE);
    }

}
