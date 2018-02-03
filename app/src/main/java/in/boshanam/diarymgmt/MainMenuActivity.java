package in.boshanam.diarymgmt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Random;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private static int MAXIMUM=50;
    private Button farmarRegister;
   private  Random random;
   private  HashSet<Integer> id ;
   private int genNextId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        random=new Random();
        id= new HashSet<Integer>();
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
        findViewById(R.id.farmerRegister).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        /*Intent intent = new Intent(this, FarmerActivity.class);
        startActivity(intent);*/
        switch (view.getId()) {
            case R.id.farmerRegister:
                generateID();
                break;
            case R.id.rateEntry: rateEntry();
                break;
            case R.id.milkCollect:
                break;
            case R.id.fatEntry:
                break;
            case R.id.paymentReport:
                break;

        }
    }

    private void rateEntry() {
    }


    private void generateID() {

         genNextId=random.nextInt(MAXIMUM);
         int count=0;

        if(!id.contains(genNextId)) {
            id.add(genNextId);
            Intent famarActivity=new Intent(this,FarmerActivity.class);
            famarActivity.putExtra("generateId" ,genNextId );
            startActivity(famarActivity);

            
        }
        else {

            generateID();
            count++;
            if(count==MAXIMUM) {
                Toast.makeText(this, "Sample Is Full ", Toast.LENGTH_SHORT).show();
            }
            }
        }




    }

