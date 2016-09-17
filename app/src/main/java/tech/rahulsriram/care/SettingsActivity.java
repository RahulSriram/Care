package tech.rahulsriram.care;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        android.app.ActionBar actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.home_food);
//        actionBar.OnNavigationListener(new O)


        button=(Button)findViewById(R.id.backbutton);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,AllNotifications.class));
                finish();
            }
        });

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
        updateIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("update_interval", (Integer) updateIntervalSpinner.getSelectedItem());
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO: empty-stub method
            }
        });

        radius = (EditText) findViewById(R.id.radius);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("radius",10);
        editor.apply();
        radius.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                radius.setText(String.valueOf(sp.getInt("radius", 10)));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Integer.parseInt(radius.getText().toString())>10) {
                    SharedPreferences.Editor editor = sp.edit();
                    String input = radius.getText().toString();
                    editor.putInt("radius",Integer.parseInt(input));
                    editor.apply();
                }
                else{
                    SharedPreferences.Editor editor = sp.edit();
                    String input = radius.getText().toString();
                    editor.putInt("radius", !input.isEmpty() ? Integer.parseInt(input) : 10);
                    editor.apply();
                }
            }
        });
    }

}
