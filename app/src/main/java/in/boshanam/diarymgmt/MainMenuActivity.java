package in.boshanam.diarymgmt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.boshanam.diarymgmt.util.ui.UIHelper;

public class MainMenuActivity extends AppCompatActivity {

    public final String COLLECT_MILK = "milk";
    @BindView(R.id.farmerRegister)
    Button farmerRegister;
    @BindView(R.id.milkCollect)
    Button collectMilk;
    @BindView(R.id.fatEntry)
    Button fatEntry;
    @BindView(R.id.rateEntry)
    Button rateEntry;
    @BindView(R.id.paymentReport)
    Button paymentEntry;
    @BindView(R.id.farmerReport)
    Button farmerReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            UIHelper.logoutAndShowLogin(this, "Logged out");
//            FirebaseAuth.getInstance().signOut();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.farmerRegister)
    public void farmerRegister() {
        farmerRegistration();

    }

    @OnClick(R.id.rateEntry)
    public void setRateEntry() {
        rateEntry();

    }

    @OnClick(R.id.fatEntry)
    public void setRatEntry() {
        fatEntry();
    }

    @OnClick(R.id.milkCollect)
    public void setCollectMilk() {
        collectMilk();

    }

    @OnClick(R.id.paymentReport)
    public void getPaymentReports() {
        paymentReport();
    }

    @OnClick(R.id.farmerReport)
    public void getFarmerReports() {
        farmerReport();
    }

    private void farmerRegistration() {
        Intent famarActivity = new Intent(this, FarmerActivity.class);
        startActivity(famarActivity);

    }

    private void rateEntry() {

        startActivity(new Intent(this, RateEntryActivity.class));
    }

    private void collectMilk() {

        startActivity(new Intent(this, DateShiftSelectActivity.class).putExtra("milkType", COLLECT_MILK));
    }

    private void fatEntry() {
        startActivity(new Intent(this, DateShiftSelectActivity.class));
    }

    private void paymentReport() {
        startActivity(new Intent(this, PaymentReportActivity.class));
    }

    private void farmerReport() {
        startActivity(new Intent(this, FarmerReportActivity.class));
    }
}

