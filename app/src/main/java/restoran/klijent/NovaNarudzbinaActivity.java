package restoran.klijent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import restoran.klijent.dialog.DialogStavkaIzmena;
import domen.Narudzbina;
import domen.StavkaNarudzbine;
import restoran.klijent.komunikacija.Komunikacija;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;
import util.Konstante;


import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.activity.R;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

public class NovaNarudzbinaActivity extends AppCompatActivity {
    Spinner spinner;
    ListView listaStavki;
    Narudzbina narudzbina;
    ArrayList<StavkaNarudzbine> lista;
    ArrayAdapter<StavkaNarudzbine> listAdapter;
    private String mActivityTitle;

    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_narudzbina);
        spinner = (Spinner) findViewById(R.id.spinner_broj_stola);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.brojeviStolova,
                android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        listaStavki = (ListView) findViewById(R.id.list_stavke_narudzbine);
        Object o = getIntent().getSerializableExtra("listaStavki");
        lista = (ArrayList<StavkaNarudzbine>) o;

        listAdapter = new ArrayAdapter<StavkaNarudzbine>(
                this, android.R.layout.simple_list_item_1, lista);
        listaStavki.setAdapter(listAdapter);

        mActivityTitle = getTitle().toString();


        result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Nova narudzbina").withIcon(FontAwesome.Icon.faw_plus_circle),
                        new PrimaryDrawerItem().withName("Lista narudzbina").withIcon(FontAwesome.Icon.faw_list),
                        new SectionDrawerItem().withName("Settings"),
                        new SecondaryDrawerItem().withName("IP address").withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem().withName("Logout").withIcon(FontAwesome.Icon.faw_user)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            switch (position) {
                                case 0:
                                    intent = new Intent(NovaNarudzbinaActivity.this, ListaProizvodaActivity.class);
                                    break;
                                case 1:
                                    intent = new Intent(NovaNarudzbinaActivity.this, ListaNarudzbinaActivity.class);
                                    break;
                                case 3:
                                    intent = new Intent(NovaNarudzbinaActivity.this, SettingsActivity.class);
                                    break;
                                case 4:
                                    intent = new Intent(NovaNarudzbinaActivity.this, LoginActivity.class);
                                    break;

                            }
                            startActivity(intent);
                        }

                        return false;
                    }
                }).build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        listaStavki.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                StavkaNarudzbine s = (StavkaNarudzbine) listAdapter.getItem(position);
                DialogStavkaIzmena dsi = DialogStavkaIzmena.newInstace(s, position, new DialogStavkaIzmena.CallbackDialog() {
                    @Override
                    public void callback() {
                        listaStavki = (ListView) findViewById(R.id.list_stavke_narudzbine);
                        lista = ListaProizvodaActivity.listaStavki;
                        listAdapter = new ArrayAdapter<StavkaNarudzbine>(NovaNarudzbinaActivity.this, android.R.layout.simple_list_item_1, lista);
                        listaStavki.setAdapter(listAdapter);
                    }
                });
                dsi.show(fragmentManager, "");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stavke_narudzbine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (result.isDrawerOpen()) {
                result.closeDrawer();
                getSupportActionBar().setTitle(mActivityTitle);
            } else {
                result.openDrawer();
                getSupportActionBar().setTitle("Navigacija");
            }

            return true;
        }
        if (id == R.id.action_settings) {

            SacuvajNarudzbinuTask task = new SacuvajNarudzbinuTask();
            task.execute();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private class SacuvajNarudzbinuTask extends AsyncTask<Void, Void, Void> {
        int ukupanIznosNarudzbine = 0;

        @Override
        protected void onPreExecute() {
            narudzbina = new Narudzbina();
            narudzbina.setBrojStola(Integer.parseInt(spinner.getSelectedItem().toString()));
            narudzbina.setDatumNarudzbine(new Date());
            narudzbina.setStatus("Neplaceno");
            int rbStavke = 1;
            for (StavkaNarudzbine stavkaNarudzbine : lista) {
                stavkaNarudzbine.setNarudzbina(narudzbina);
                stavkaNarudzbine.setRbStavke(rbStavke);
                rbStavke++;
                ukupanIznosNarudzbine += stavkaNarudzbine.getIznos();
            }
            narudzbina.setListaStavki(lista);
            narudzbina.setUkupanIznos(ukupanIznosNarudzbine);


        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Komunikacija k = new Komunikacija();
                TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
                toZahtev.setOperacija(Konstante.SACUVAJ_NARUDZBINU);
                toZahtev.setParametar(narudzbina);
                k.posaljiZahtev(toZahtev);
                TransferObjekatOdgovor toOdgovor = k.procitajOdgovor();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(NovaNarudzbinaActivity.this, "Uspesno poslata narudzbina", Toast.LENGTH_SHORT).show();
            ListaProizvodaActivity.listaStavki = new ArrayList<>();
            startActivity(new Intent(NovaNarudzbinaActivity.this, ListaNarudzbinaActivity.class));
        }
    }
}
