package com.example.klijentandr2;

import java.io.IOException;
import java.net.UnknownHostException;

import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;
import util.Konstante;
import komunikacija.Komunikacija;
import domen.Konobar;
import utility.SnackBarUtility;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText etUserName, etPassword;
    ProgressDialog progressDialog;
    MaterialDialog md;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btnLogIn);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);

    }

    public ProgressDialog getProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public MaterialDialog getProgressDialogMaterial() {
        md = new MaterialDialog.Builder(this)
                .title(R.string.dialog_title_login)
                .content(R.string.dialog_tekst_login)
                .progress(true, 0).show();
        return md;
    }

    public void dismissDialogMaterial() {
        if (md!=null){
            md.setCancelable(false);
            md.dismiss();
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.setCancelable(false);
            progressDialog.dismiss();
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
        SnackBarUtility.prikaziSnackBar(parent);
    }

    public void logovanje(boolean login) {
        if (login) {

            Intent intent = new Intent(this, ListaNarudzbinaActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Uspesno ste se ulogovali", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(this, "Ne postoji takav konobar", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private class LoginTask extends AsyncTask<Konobar, String, Boolean> {
        TransferObjekatZahtev toZahtev;
        TransferObjekatOdgovor toOdgovor;

        @Override
        protected void onPreExecute() {
            getProgressDialogMaterial();
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
//					onProgressUpdate("IO exception");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dismissDialogMaterial();
            if (toOdgovor == null) {
                greska();
            } else {
                logovanje(result);
            }

        }

    }
}
