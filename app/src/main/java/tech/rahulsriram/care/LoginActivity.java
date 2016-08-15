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

    String phoneNumber,deviceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login =(Button)findViewById(R.id.loginButton);
        assert login != null;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(LoginActivity.this,getPhoneNumber(), Toast.LENGTH_LONG).show();
                Toast.makeText(LoginActivity.this,getDeviceId(), Toast.LENGTH_LONG).show();
                Intent intent =new Intent(LoginActivity.this,ItemSelectionActivity.class);
                startActivity(intent);

            }
        });

    }
    public String getPhoneNumber()
    {
        EditText pn = (EditText)findViewById(R.id.mobileNumberInput);
        phoneNumber = pn.getText().toString();
        return phoneNumber;

    }

    public String getDeviceId()
    {

        deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

          return deviceId;
    }

}

