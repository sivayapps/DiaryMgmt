package in.boshanam.diarymgmt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import in.boshanam.diarymgmt.listeners.CustomOnItemSelectedListener;

public class FarmerActivity extends AppCompatActivity {
    /*private static final String[] TYPE_MILK = new String[] {
            "BUFFlO", "OX"
    };*/
   private Spinner  milkType;
   private Button register;
    private EditText farmername;
    private EditText id;
    private EditText farmerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();



    }

    public void addListenerOnSpinnerItemSelection() {
        milkType = (Spinner) findViewById(R.id.spinner1);
        milkType.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }
    public void addListenerOnButton() {

        milkType = (Spinner) findViewById(R.id.spinner1);
        farmerId=(EditText)findViewById(R.id.generateId);
        farmername=(EditText)findViewById(R.id.farmerName);

        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(FarmerActivity.this,"OnClickListener : "+String.valueOf(milkType.getSelectedItem()),Toast.LENGTH_SHORT).show();
            }

        });
    }


}
