package in.boshanam.diarymgmt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuActivity extends AppCompatActivity {
    private static int MAXIMUM = 50;
    public final String COLLECT_MILK="milk";
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

    private Random random;
    private HashSet<Integer> id;
    private int genNextId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        random = new Random();
        id = new HashSet<Integer>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.farmerRegister)
    public void farmerRegister() {
        generateID();

    }

    @OnClick(R.id.rateEntry)
    public void setRateEntry() {
        rateEntry();

    }

    @OnClick(R.id.fatEntry)
    public void setratEntry() {
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


    private void generateID() {
        genNextId = random.nextInt(MAXIMUM);
        int count = 0;
        if (!id.contains(genNextId)) {
            id.add(genNextId);
            Intent famarActivity = new Intent(this, FarmerActivity.class);
            famarActivity.putExtra("generateId", genNextId);
            startActivity(famarActivity);
        } else {
            generateID();
            count++;
            if (count == MAXIMUM) {
                Toast.makeText(this, "Samples Is Full ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void rateEntry() {

        startActivity(new Intent(this, RateEntryActivity.class));
    }

    private void collectMilk() {

        startActivity(new Intent(this, DateShiftSelectActivity.class).putExtra("milkType",COLLECT_MILK));
    }

    private void fatEntry() {
        startActivity(new Intent(this, DateShiftSelectActivity.class));
    }

    private void paymentReport() {
        startActivity(new Intent(this, FatEntryActivity.class));
    }
}

