package restoran.klijent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.activity.R;

import restoran.klijent.komunikacija.Komunikacija;
import restoran.klijent.utility.Utility;


public class SettingsActivity extends Activity {
    EditText etIPAdress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        etIPAdress = (EditText) findViewById(R.id.etIPAddress);
        etIPAdress.setText(Komunikacija.ipAddress);

    }
    public void postaviIP(View view){
        if (etIPAdress.getText()!= null && !"".equalsIgnoreCase(etIPAdress.getText().toString()) ){
            Komunikacija.ipAddress= etIPAdress.getText().toString();
            SharedPreferences.Editor editor = getSharedPreferences(LoginActivity.PREFS_IP_ADDRESS, MODE_PRIVATE).edit();
            editor.putString("ipAddress", etIPAdress.getText().toString());
            editor.commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            Utility.prikaziSnackBar(etIPAdress,"Niste uneli adresu");
        }
    }

}
