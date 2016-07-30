package tech.rahulsriram.care;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView text = (TextView) findViewById(R.id.mobileNumberInput);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callMapsActivity = new Intent(LoginActivity.this, MapsActivity.class);
                LoginActivity.this.startActivity(callMapsActivity);
            }
        });
    }
}

