package tech.rahulsriram.care;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    String deviceId;
    EditText phoneNumber, countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        countryCode = (EditText) findViewById(R.id.countryCode);
        phoneNumber = (EditText) findViewById(R.id.mobileNumber);
        deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        Button login = (Button) findViewById(R.id.loginButton);
        assert login != null;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = countryCode.getText().toString();
                if(code.length() == 0) {
                    code = getString(R.string.default_country_code);
                }

                String number = code + phoneNumber.getText().toString();

                if (number.length() >= 12) {
                    Toast.makeText(getApplicationContext(), number + "\n" + deviceId, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, ItemSelectionActivity.class));
                    startService(new Intent(LoginActivity.this, CareService.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), number + " isn't a valid phone number", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}

