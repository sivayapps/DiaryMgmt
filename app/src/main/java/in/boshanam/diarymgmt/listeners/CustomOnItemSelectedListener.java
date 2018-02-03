package in.boshanam.diarymgmt.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by user on 2/2/2018.
 */

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
