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
    ArrayList<String> selection = new ArrayList<String>();
    FloatingActionButton donate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemselectionactivity);
        Switch foodSwitch = (Switch) findViewById(R.id.FoodSwitch);
        foodSwitch.setOnCheckedChangeListener(this);g
        Switch packedFoodSwitch = (Switch) findViewById(R.id.PackedFoodSwitch);
        packedFoodSwitch.setOnCheckedChangeListener(this);
        Switch clothesSwitch = (Switch) findViewById(R.id.ClothesSwitch);
        clothesSwitch.setOnCheckedChangeListener(this);
        Switch bookSwitch = (Switch) findViewById(R.id.BookSwitch);
        bookSwitch.setOnCheckedChangeListener(this);
        donate = (FloatingActionButton) findViewById(R.id.donate);
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
        if(isChecked)
            selection.add(buttonView.getText().toString());
        else
            selection.remove(buttonView.getText().toString());
    }
}
