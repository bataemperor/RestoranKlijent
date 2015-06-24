package restoran.klijent;

import java.io.IOException;

import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;
import util.Konstante;
import restoran.klijent.komunikacija.Komunikacija;
import domen.Konobar;
import restoran.klijent.utility.Utility;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.activity.R;


public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText etUserName, etPassword;
    public static final String PREFS_LOGIN = "Provera logina";
    public static final String PREFS_IP_ADDRESS = "IP adresa";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btnLogIn);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        ipAddressSet();
        loginShared();

    }

    private void ipAddressSet() {
        sharedPreferences = getSharedPreferences(PREFS_IP_ADDRESS,MODE_PRIVATE);
        String ipAdresa = sharedPreferences.getString("ipAddress", null);
        if (ipAdresa!=null) {
            Komunikacija.ipAddress = ipAdresa;
        }

    }

    private void loginShared() {
        sharedPreferences = getSharedPreferences(PREFS_LOGIN, MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);
        if (username!=null && password!=null) {
            etUserName.setText(username);
            etPassword.setText(password);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void provera(View view) {
        if (etUserName.getText().toString().length() == 0) {
            etUserName.setError("Username je obavezan!");
            return;
        }
        if (etPassword.getText().toString().length() == 0) {
            etPassword.setError("Password je obavezan!");
            return;
        }

        Konobar k = new Konobar();
        k.setUsername(etUserName.getText().toString());
        k.setPassword(etPassword.getText().toString());
        LoginTask lt = new LoginTask();
        lt.execute(k);

    }

    public void greska() {
        RelativeLayout parent = (RelativeLayout)findViewById(R.id.login_layout);
        Utility.prikaziSnackBar(parent, Utility.SNACKBAR_NEUSPESNA_KONEKCIJA, Utility.SNACKBAR_ACTION_CHANGE_IP, new Utility.SnackbarCallback() {
            @Override
            public void callback() {
                Intent intent =  new Intent(LoginActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void logovanje(boolean login) {
        if (login) {
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_LOGIN, MODE_PRIVATE).edit();
            editor.putString("username", etUserName.getText().toString());
            editor.putString("password", etPassword.getText().toString());
            editor.commit();
            Intent intent = new Intent(this, ListaNarudzbinaActivity.class);
            startActivity(intent);
            Toast.makeText(this, Utility.SNACKBAR_USPESAN_LOGIN, Toast.LENGTH_SHORT)
                    .show();
        } else {
            Utility.prikaziSnackBar(findViewById(R.id.login_layout), Utility.SNACKBAR_NEUSPESAN_LOGIN);
        }

    }

    private class LoginTask extends AsyncTask<Konobar, String, Boolean> {
        TransferObjekatZahtev toZahtev;
        TransferObjekatOdgovor toOdgovor;

        @Override
        protected void onPreExecute() {
            Utility.getProgressDialogMaterial(LoginActivity.this);
        }

        @Override
        protected Boolean doInBackground(Konobar... params) {
            toZahtev = new TransferObjekatZahtev();
            toZahtev.setOperacija(Konstante.PRONADJI_KONOBARE);
            toZahtev.setParametar(params[0]);
            Komunikacija k;

            try {
                k = new Komunikacija();
                k.posaljiZahtev(toZahtev);
                toOdgovor = k.procitajOdgovor();
                return (Boolean) toOdgovor.getRezultat();
            } catch (IOException | ClassNotFoundException e) {
                // TODO Auto-generated catch block
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Utility.dismissDialogMaterial();
            if (toOdgovor == null) {
                greska();
            } else {
                logovanje(result);
            }

        }

    }
}