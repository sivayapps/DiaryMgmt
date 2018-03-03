package in.boshanam.diarymgmt;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import in.boshanam.diarymgmt.app.constants.AppConstants;
import in.boshanam.diarymgmt.app.constants.FarmerConstants;
import in.boshanam.diarymgmt.command.ListenerAdapter;
import in.boshanam.diarymgmt.domain.BaseAppCompatActivity;
import in.boshanam.diarymgmt.domain.Farmer;
import in.boshanam.diarymgmt.domain.MilkType;
import in.boshanam.diarymgmt.model.viewmodel.FarmerViewModel;
import in.boshanam.diarymgmt.repository.FireBaseDao;
import in.boshanam.diarymgmt.tableview.TableViewHelper;
import in.boshanam.diarymgmt.tableview.model.TableViewModelDef;
import in.boshanam.diarymgmt.util.StringUtils;
import in.boshanam.diarymgmt.util.ui.UIHelper;

public class FarmerActivity extends BaseAppCompatActivity {

    public static final String TAG = "FarmerActivity";
    @BindView(R.id.farmerId)
    EditText farmerId;

    @BindView(R.id.farmerName)
    EditText farmerName;

    @BindView(R.id.milkType)
    Spinner milkType;

    @BindView(R.id.register)
    Button register;

    @BindView(R.id.farmerListingTableView)
    TableView farmerListingTableView;

    private ListenerRegistration listenerRegistration;

    private FarmerViewModel farmerViewModel;
    private Map<String, Farmer> farmerByFarmerId = new HashMap<>();
    private boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);
        ButterKnife.bind(this);
        findViewById(R.id.farmer_loadingProgressPanel).setVisibility(View.GONE);
        //TODO generate next available ID for farmer
        String dairyId = getDairyID();
        farmerViewModel = ViewModelProviders.of(this).get(FarmerViewModel.class);
        farmerViewModel.init(dairyId);
        farmerViewModel.getFarmerLiveData().getFarmerIdFarmerCacheLiveData().observe(this, new Observer<Map<String, Farmer>>() {
            @Override
            public void onChanged(@Nullable Map<String, Farmer> farmerByFarmerId) {
                FarmerActivity.this.farmerByFarmerId = farmerByFarmerId;
            }
        });
        farmerViewModel.getFarmerLiveData().getFarmerDocSnapshotLiveData().observe(this, new Observer<List<DocumentSnapshot>>() {
            @Override
            public void onChanged(@Nullable List<DocumentSnapshot> documentSnapshots) {

                TableViewHelper tableViewHelper = TableViewHelper.buildTableViewHelper(FarmerActivity.this,
                        farmerListingTableView,
                        new TableViewModelDef(FarmerConstants.FarmerDataGrid.class), true);
                tableViewHelper.initTableWithData(FarmerActivity.this, documentSnapshots);
                findViewById(R.id.farmer_loadingProgressPanel).setVisibility(View.GONE);
                if (loading) {
                    //offline save don't call listeners, but data change will trigger this
                    //so clearing fields when save started and we see updates in data
                    loading = false;
                    clearFormFields();
                }
            }
        });
    }

    @OnClick(R.id.register)
    public void registerFarmer() {
        if (validate()) {
            saveFarmer();
        }
    }

    @OnFocusChange(R.id.farmerId)
    public void onFocusChanged(boolean focused) {
        if (focused) {
            return;
        }
        String farmerID = farmerId.getText().toString();
        String dairyId = getDairyID();
        if (StringUtils.isNotBlank(farmerID) && StringUtils.isNotBlank(dairyId)) {
            Farmer farmer = farmerByFarmerId.get(farmerID.trim());
            if (farmer != null) {
                if (StringUtils.isNotBlank(farmer.getName())) {
                    farmerName.setText(farmer.getName());
                }
                if (farmer.getMilkType() != null) {
                    UIHelper.setSpinnerSelection(milkType, farmer.getMilkType().getIndex());
                }
            } else {
                farmerName.setText("");
            }
        }
    }

    private void saveFarmer() {
        String dairyId = getDairyID();
        if (StringUtils.isBlank(dairyId)) {
            UIHelper.logoutAndShowLogin(this, "Dairy Information not available, logging out to reload");
            return;
        }
        final Farmer farmer = new Farmer();
        farmer.setUpdateTime(new Date());
        farmer.setDairyId(dairyId);
        farmer.setId(farmerId.getText().toString());
        farmer.setName(farmerName.getText().toString());
        if (milkType.getSelectedItemPosition() >= 0) {
            farmer.setMilkType(MilkType.getMilkTypeForIndex(milkType.getSelectedItemPosition()));
        }
        findViewById(R.id.farmer_loadingProgressPanel).setVisibility(View.VISIBLE);
        loading = true;
        FireBaseDao.saveFarmer(this, farmer, new ListenerAdapter<Farmer>() {
            @Override
            public void onSuccess(Farmer data) {
                Toast.makeText(getApplicationContext(), "Successfully Save", Toast.LENGTH_LONG).show();
                clearFormFields();
                findViewById(R.id.farmer_loadingProgressPanel).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                findViewById(R.id.farmer_loadingProgressPanel).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Failed to Save Farmer, please retry.", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Failed to save Farmer", e);
            }
        });
    }

    private void clearFormFields() {
        farmerId.setText("");
        farmerName.setText("");
    }

    protected String getDairyID() {
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.DAIRY_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(AppConstants.DAIRY_ID_KEY, null);
    }

    private boolean validate() {
        if (StringUtils.isBlank(farmerName.getText().toString())) {
            Toast.makeText(this, "Name should not be blank", Toast.LENGTH_SHORT).show();
            return false;
        }
        String farmerID = farmerId.getText().toString();
        if (StringUtils.isBlank(farmerID)) {
            Toast.makeText(this, "Farmer Id should not be blank", Toast.LENGTH_SHORT).show();
            return false;
        }
        String dairyId = getDairyID();
        if (StringUtils.isBlank(dairyId)) {
            UIHelper.logoutAndShowLogin(this, "Dairy Information not available, logging out for reload");
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
