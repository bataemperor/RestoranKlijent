package com.example.klijentandr2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import domen.Narudzbina;
import domen.Proizvod;
import domen.StavkaNarudzbine;
import komunikacija.Komunikacija;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;
import util.Konstante;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class NovaNarudzbinaActivity extends AppCompatActivity {
    Spinner spinner;
    ListView listaStavki;
    Narudzbina narudzbina;
    ArrayList<StavkaNarudzbine> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        ArrayAdapter<StavkaNarudzbine> listAdapter = new ArrayAdapter<StavkaNarudzbine>(
                this, android.R.layout.simple_list_item_1, lista);
        listaStavki.setAdapter(listAdapter);

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
            }
            narudzbina.setListaStavki(lista);


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

            } catch (IOException|ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(NovaNarudzbinaActivity.this, "Uspesno poslata narudzbina", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(NovaNarudzbinaActivity.this, ListaNarudzbinaActivity.class));
        }
    }
}
