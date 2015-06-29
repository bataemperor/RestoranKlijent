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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.activity.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import domen.Narudzbina;
import domen.StavkaNarudzbine;
import restoran.klijent.dialog.DialogStavkaIzmena;
import restoran.klijent.komunikacija.Komunikacija;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;
import util.Konstante;

public class IzmenaNarudzbineActivity extends AppCompatActivity {
    Spinner spinner;
    ListView listaStavki;
    Narudzbina narudzbina;
    ArrayList<StavkaNarudzbine> lista;
    ArrayAdapter<StavkaNarudzbine> listAdapter;
    public static List<StavkaNarudzbine> listaStavkiNarudzbine = new ArrayList<StavkaNarudzbine>();

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
        Object o = getIntent().getSerializableExtra("narudzbina");
        narudzbina = (Narudzbina) o;
        listaStavkiNarudzbine = narudzbina.getListaStavki();
        setTitle("Narudzbina ID : " + narudzbina.getNarudzbinaID());
        listAdapter = new ArrayAdapter<StavkaNarudzbine>(
                this, android.R.layout.simple_list_item_1, listaStavkiNarudzbine);
        listaStavki.setAdapter(listAdapter);
        spinner.setSelection(narudzbina.getBrojStola() - 1);

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
                DialogStavkaIzmena dsi = DialogStavkaIzmena.newInstace(s, position, new DialogStavkaIzmena.CallbackDialog() {
                    @Override
                    public void callback() {
                        listaStavki = (ListView) findViewById(R.id.list_stavke_narudzbine_izmena);
                        lista = ListaProizvodaActivity.listaStavki;
                        listAdapter = new ArrayAdapter<StavkaNarudzbine>(IzmenaNarudzbineActivity.this, android.R.layout.simple_list_item_1, lista);
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
        if (id == R.id.action_settings) {

            SacuvajNarudzbinuTask task = new SacuvajNarudzbinuTask();
            task.execute();

            return true;
        }
        return super.onOptionsItemSelected(item);
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
            Toast.makeText(IzmenaNarudzbineActivity.this, "Uspesno poslata narudzbina", Toast.LENGTH_SHORT).show();
            ListaProizvodaActivity.listaStavki = new ArrayList<>();
            startActivity(new Intent(IzmenaNarudzbineActivity.this, ListaNarudzbinaActivity.class));
        }
    }
}
