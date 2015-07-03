package restoran.klijent;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.activity.R;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import domen.Narudzbina;
import domen.StavkaNarudzbine;

import restoran.klijent.dialog.DialogStavkaUpdate;
import restoran.klijent.komunikacija.Komunikacija;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;
import util.Konstante;

public class IzmenaNarudzbineActivity extends AppCompatActivity {
    public static boolean izmena = false;
    Spinner spinner;
    ListView listaStavki;
    public static Narudzbina narudzbina;
//    ArrayList<StavkaNarudzbine> lista;
    ArrayAdapter<StavkaNarudzbine> listAdapter;
    public static List<StavkaNarudzbine> listaStavkiNarudzbine = new ArrayList<StavkaNarudzbine>();
    private String mActivityTitle;

    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izmena_narudzbine);
        spinner = (Spinner) findViewById(R.id.spinner_broj_stola_izmena);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.brojeviStolova,
                android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        listaStavki = (ListView) findViewById(R.id.list_stavke_narudzbine_izmena);
        if (!izmena) {
            Object o = getIntent().getSerializableExtra("narudzbina");
            narudzbina = (Narudzbina) o;
            listaStavkiNarudzbine = narudzbina.getListaStavki();
        }
        setTitle("Narudzbina ID : " + narudzbina.getNarudzbinaID());
        listAdapter = new ArrayAdapter<StavkaNarudzbine>(
                this, android.R.layout.simple_list_item_1, listaStavkiNarudzbine);
        listaStavki.setAdapter(listAdapter);
        spinner.setSelection(narudzbina.getBrojStola() - 1);

        mActivityTitle = getTitle().toString();
        izmena = false;

        result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
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
                                    intent = new Intent(IzmenaNarudzbineActivity.this, ListaProizvodaActivity.class);
                                    break;
                                case 1:
                                    intent = new Intent(IzmenaNarudzbineActivity.this, ListaNarudzbinaActivity.class);
                                    break;
                                case 3:
                                    intent = new Intent(IzmenaNarudzbineActivity.this, SettingsActivity.class);
                                    break;
                                case 4:
                                    intent = new Intent(IzmenaNarudzbineActivity.this, LoginActivity.class);
                                    break;

                            }
                            startActivity(intent);
                        }

                        return false;
                    }
                }).build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        postaviFloatButton();
    }

    private void postaviFloatButton() {

        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_new));

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon).setBackgroundDrawable(R.drawable.selector_button)
                .build();
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                izmena = true;
                Intent intent = new Intent(IzmenaNarudzbineActivity.this, ListaProizvodaActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        listaStavki.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                StavkaNarudzbine s = listAdapter.getItem(position);
                DialogStavkaUpdate dsu = DialogStavkaUpdate.newInstace(s, position, new DialogStavkaUpdate.CallbackDialog() {
                    @Override
                    public void callback() {
                        listaStavki = (ListView) findViewById(R.id.list_stavke_narudzbine_izmena);
//                        lista = ListaProizvodaActivity.listaStavki;
                        listAdapter = new ArrayAdapter<StavkaNarudzbine>(IzmenaNarudzbineActivity.this, android.R.layout.simple_list_item_1, listaStavkiNarudzbine);
                        listaStavki.setAdapter(listAdapter);
                    }
                });
                dsu.show(fragmentManager, "");
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

            IzmeniNarudzbinuTask task = new IzmeniNarudzbinuTask();
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

    private class IzmeniNarudzbinuTask extends AsyncTask<Void, Void, Void> {
        int ukupanIznosNarudzbine = 0;
        TransferObjekatOdgovor toOdgovor;
        @Override
        protected void onPreExecute() {
//            narudzbina = new Narudzbina();
            narudzbina.setBrojStola(Integer.parseInt(spinner.getSelectedItem().toString()));
//            narudzbina.setDatumNarudzbine(new Date());
//            narudzbina.setStatus("Neplaceno");
            int rbStavke = 1;
            for (StavkaNarudzbine stavkaNarudzbine : listaStavkiNarudzbine) {
                stavkaNarudzbine.setNarudzbina(narudzbina);
                stavkaNarudzbine.setRbStavke(rbStavke);
                rbStavke++;
                ukupanIznosNarudzbine += stavkaNarudzbine.getIznos();
            }
            narudzbina.setListaStavki(listaStavkiNarudzbine);
            narudzbina.setUkupanIznos(ukupanIznosNarudzbine);


        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Komunikacija k = new Komunikacija();
                TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
                toZahtev.setOperacija(Konstante.IZMENI_NARUDZBINU);
                toZahtev.setParametar(narudzbina);
                k.posaljiZahtev(toZahtev);
                toOdgovor = k.procitajOdgovor();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            Toast.makeText(IzmenaNarudzbineActivity.this, toOdgovor.getOdgovor(), Toast.LENGTH_SHORT).show();
            new MaterialDialog.Builder(IzmenaNarudzbineActivity.this)
                    .content("Uspesno ste izmenili narudzbinu!")
                    .negativeText("OK")
                    .title("Izmena narudzbine")
                    .iconRes(R.drawable.ok)
                    .limitIconToDefaultSize()
                    .positiveText("")
                    .cancelable(false)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            ListaProizvodaActivity.listaStavki = new ArrayList<>();
                            startActivity(new Intent(IzmenaNarudzbineActivity.this, ListaNarudzbinaActivity.class));
                        }
                    })
                    .show();


        }
    }
}
