package in.boshanam.diarymgmt;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectMilkActivity extends Activity {
    @BindView(R.id.collect_milk_date)
    TextView dateView;
    @BindView(R.id.collect_milk_shift)
    TextView shiftView;
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

}
