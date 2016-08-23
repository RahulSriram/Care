package tech.rahulsriram.care;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sp;
    Switch volunteerSwitch;
    Spinner updateIntervalSpinner;
    EditText radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sp = getSharedPreferences("Care", MODE_PRIVATE);

        volunteerSwitch = (Switch) findViewById(R.id.volunteer_switch);
        volunteerSwitch.setChecked(sp.getBoolean("volunteer", false));
        volunteerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("volunteer", isChecked);
                editor.apply();
            }
        });

        final List<Integer> updateInterval = new ArrayList<>();
        for (int i = 15; i <= 60; i += 5) {
            updateInterval.add(i);
        }
        updateIntervalSpinner = (Spinner) findViewById(R.id.update_interval);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, updateInterval);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateIntervalSpinner.setAdapter(adapter);
        updateIntervalSpinner.setSelection(updateInterval.indexOf(sp.getInt("update_interval", 15)));
        updateIntervalSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("update_interval", (Integer) updateIntervalSpinner.getSelectedItem());
                editor.apply();
            }
        });

        radius = (EditText) findViewById(R.id.radius);
        radius.setText(String.valueOf(sp.getInt("radius", 10)));
        radius.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                SharedPreferences.Editor editor = sp.edit();
                String input = radius.getText().toString();
                editor.putInt("radius", !input.isEmpty()? Integer.parseInt(input) : 10);
                editor.apply();
                return true;
            }
        });
    }
}