package com.example.klijentandr2;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import domen.Narudzbina;
import komunikacija.Komunikacija;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;
import util.Konstante;


public class ListaNarudzbinaActivity extends AppCompatActivity{
	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_narudzbina);
		listView = (ListView) findViewById(R.id.lista_narudzbina);
		postaviFloatButton();
	}

	@Override
	protected void onResume() {
		super.onResume();
		new GetNarudzbineTask().execute();
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
				novaNarudzbina();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first, menu);
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
	public void novaNarudzbina(){
		Intent intent = new Intent(this,ListaProizvodaActivity.class);
		startActivity(intent);
	}

	private class GetNarudzbineTask extends AsyncTask<Void,Void,List<Narudzbina>>{
		TransferObjekatZahtev zahtev;
		TransferObjekatOdgovor odgovor;
		List<Narudzbina> listaNarudzbina;
		ArrayAdapter<Narudzbina> adapter;
		@Override
		protected void onPreExecute() {
			listaNarudzbina = new ArrayList<>();
		}

		@Override
		protected List<Narudzbina> doInBackground(Void... params) {
			zahtev = new TransferObjekatZahtev();
			zahtev.setOperacija(Konstante.VRATI_SVE_NARUDZBINE);
			try {
				Komunikacija k = new Komunikacija();
				k.posaljiZahtev(zahtev);
				odgovor = k.procitajOdgovor();
				listaNarudzbina = (List<Narudzbina>) odgovor.getRezultat();
			} catch (IOException|ClassNotFoundException e) {
				e.printStackTrace();
			}

			return listaNarudzbina;
		}

		@Override
		protected void onPostExecute(List<Narudzbina> narudzbinas) {
			adapter = new ArrayAdapter<Narudzbina>(ListaNarudzbinaActivity.this,android.R.layout.simple_list_item_1,listaNarudzbina);
			listView.setAdapter(adapter);
		}
	}
}
