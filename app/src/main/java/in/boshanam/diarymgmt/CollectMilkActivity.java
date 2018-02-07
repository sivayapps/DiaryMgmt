package in.boshanam.diarymgmt;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectMilkActivity extends Activity {
    @BindView(R.id.collect_milk_date)
    TextView dateView;
    @BindView(R.id.collect_milk_shift)
    TextView shiftView;
    @BindView(R.id.register_farmer_id)
    EditText registerFormerId;
    @BindView(R.id.farmer_sample_id)
    EditText farmerSampleId;
    @BindView(R.id.collect_farmer_milk)
    EditText collectMilk;
    @BindView(R.id.find_farmer_milk)
    Button findFarmerMilk;
    @BindView(R.id.collect_milk_save)
    Button saveMilk;

    private String date;
    private String shift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_milk);
        ButterKnife.bind(this);

        Intent iin = getIntent();
        savedInstanceState = iin.getExtras();
        date = (String.valueOf(savedInstanceState.get("date")));
        shift = (String.valueOf(savedInstanceState.get("shift")));
        dateView.setText(date);
        shiftView.setText(shift);
    }

    @OnClick(R.id.find_farmer_milk)
    public void getMilk() {
        if (findValidate()) {

        }
    }

    @OnClick(R.id.collect_milk_save)
    public void saveMilk() {
        if (saveValidate()) {

        }
    }

    private boolean findValidate() {
        if (registerFormerId.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "pls fill the empty farmer id", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean saveValidate() {
        if (registerFormerId.getText().toString().trim().length() == 0 || farmerSampleId.getText().toString().trim().length() == 0 ||
                collectMilk.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "pls fill the empty Field", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
