package tech.rahulsriram.care;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText mobileNumber = (EditText) findViewById(R.id.mobileNumberInput);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Number: " + mobileNumber.getText().toString(), Toast.LENGTH_LONG).show();
                Intent callMapsActivity = new Intent(LoginActivity.this, MapsActivity.class);
                LoginActivity.this.startActivity(callMapsActivity);
            }
        });
    }
}

