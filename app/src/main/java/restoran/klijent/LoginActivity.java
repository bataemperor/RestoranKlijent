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
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.activity.R;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;


public class LoginActivity extends AppCompatActivity {
    public static final long BACK_PRESS_TIMEFRAME = 5 * 1000;
    private long lastBackPressTs = 0;
    Toast backToast;
    Button btnLogin;
    EditText etUserName, etPassword;
    public static final String PREFS_LOGIN = "Provera logina";
    public static final String PREFS_IP_ADDRESS = "IP adresa";
    SharedPreferences sharedPreferences;
    private String mActivityTitle;

    private Drawer result = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btnLogIn);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        ipAddressSet();
        loginShared();
        mActivityTitle = getTitle().toString();

        result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Nova narudzbina").withIcon(FontAwesome.Icon.faw_plus_circle).setEnabled(false),
                        new PrimaryDrawerItem().withName("Lista narudzbina").withIcon(FontAwesome.Icon.faw_list).setEnabled(false),
                        new SectionDrawerItem().withName("Settings"),
                        new SecondaryDrawerItem().withName("IP address").withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem().withName("Logout").withIcon(FontAwesome.Icon.faw_user).setEnabled(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            switch (position) {
                                case 0:
                                    intent = new Intent(LoginActivity.this, ListaProizvodaActivity.class);
                                    break;
                                case 1:
                                    intent = new Intent(LoginActivity.this, ListaNarudzbinaActivity.class);
                                    break;
                                case 3:
                                    intent = new Intent(LoginActivity.this, SettingsActivity.class);
                                    break;
                                case 4:
                                    intent = new Intent(LoginActivity.this, LoginActivity.class);
                                    break;

                            }
                            startActivity(intent);
                        }

                        return false;
                    }
                }).withSelectedItem(3).build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        animation();
    }

    private void animation() {
        YoYo.with(Techniques.FadeInDown).duration(3000).playOn(findViewById(R.id.layout_restoran_klijent));
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
        etUserName.setSelection(etUserName.getText().length());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if  (id == android.R.id.home){
            if (result.isDrawerOpen()){
                result.closeDrawer();
                getSupportActionBar().setTitle(mActivityTitle);
            }else {
                result.openDrawer();
                getSupportActionBar().setTitle("Navigacija");
            }

            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
            return;
        }
        if (!(System.currentTimeMillis() - lastBackPressTs <= BACK_PRESS_TIMEFRAME))
        {
            backToast = Toast.makeText(LoginActivity.this,"Pritisnite ponovo Nazad da bi ste izašli", Toast.LENGTH_LONG);
            backToast.show();
            lastBackPressTs = System.currentTimeMillis();
        }
        else
        {
            lastBackPressTs = 0;
            if (backToast != null) backToast.cancel();
            setResult(RESULT_OK);
            finish();
        }
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
