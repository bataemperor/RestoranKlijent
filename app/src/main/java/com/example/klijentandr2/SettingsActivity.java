package com.example.klijentandr2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import komunikacija.Komunikacija;
import utility.Utility;


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
        if (etIPAdress.getText().toString()!= null && !"".equalsIgnoreCase(etIPAdress.getText().toString()) ){
            Komunikacija.ipAddress= etIPAdress.getText().toString();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            Utility.prikaziSnackBar(etIPAdress,"Niste uneli adresu");
        }
    }

}
