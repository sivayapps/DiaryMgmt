package in.boshanam.diarymgmt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import in.boshanam.diarymgmt.domain.RateEntry;

import java.util.ArrayList;
import java.util.List;

public class RateEntryActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Spinner milkType;
    private EditText fat;
    private EditText rate;
    private Button next;
    private Button finish;
    private final String MILK_TYPE = "COW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_entry);


        milkType = (Spinner) findViewById(R.id.milk_type);
        fat = (EditText) findViewById(R.id.fat);
        rate = (EditText) findViewById(R.id.rate);
        next = (Button) findViewById(R.id.next);
        finish = (Button) findViewById(R.id.finish);
        findViewById(R.id.next).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (validate()) {
            if (MILK_TYPE.equals(milkType.getSelectedItem().toString())) {
               final RateEntry rateEntry = new RateEntry();



            } else {


            }
        }
    }

    private boolean validate() {
        if (fat.length() == 0 || rate.length() == 0) {

            Toast.makeText(getApplicationContext(), "pls fill the empty fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        Toast.makeText(parent.getContext(),
                "Milk Type Is : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

