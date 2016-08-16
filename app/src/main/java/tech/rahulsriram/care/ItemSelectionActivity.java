package tech.rahulsriram.care;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by SREEVATHSA on 15-08-2016.
 */
public class ItemSelectionActivity  extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    ArrayList<String> selection = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selection);

        Switch homeFoodSwitch = (Switch) findViewById(R.id.homeFoodSwitch);
        assert homeFoodSwitch != null;
        homeFoodSwitch.setOnCheckedChangeListener(this);

        Switch packedFoodSwitch = (Switch) findViewById(R.id.packedFoodSwitch);
        assert packedFoodSwitch != null;
        packedFoodSwitch.setOnCheckedChangeListener(this);

        Switch clothesSwitch = (Switch) findViewById(R.id.clothesSwitch);
        assert clothesSwitch != null;
        clothesSwitch.setOnCheckedChangeListener(this);

        Switch bookSwitch = (Switch) findViewById(R.id.bookSwitch);
        assert bookSwitch != null;
        bookSwitch.setOnCheckedChangeListener(this);

        FloatingActionButton donate = (FloatingActionButton) findViewById(R.id.donateButton);
        assert donate != null;
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String final_Donate = "";

                for (String selections : selection) {   //your was String selection : selection two same variable i just changed it
                    final_Donate = final_Donate + selections + "\n";
                }

                Toast.makeText(getApplicationContext(), final_Donate, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            selection.add(buttonView.getText().toString());
        } else {
            selection.remove(buttonView.getText().toString());
        }
    }
}
