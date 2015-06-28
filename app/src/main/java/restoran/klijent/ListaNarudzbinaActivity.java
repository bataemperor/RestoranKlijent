package restoran.klijent;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.activity.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import domen.Narudzbina;
import restoran.klijent.komunikacija.Komunikacija;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;
import util.Konstante;
import restoran.klijent.utility.Utility;


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
		NarudzbineAdapter adapter;
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
			if (odgovor == null) {
				Utility.prikaziSnackBar((FrameLayout) findViewById(R.id.activity_lista_narudzbina), Utility.SNACKBAR_NEUSPESNA_KONEKCIJA);
			}
			else {
				adapter = new NarudzbineAdapter(ListaNarudzbinaActivity.this,listaNarudzbina);
//				adapter = new ArrayAdapter<Narudzbina>(ListaNarudzbinaActivity.this, android.R.layout.simple_list_item_1, listaNarudzbina);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Narudzbina nar = adapter.getItem(position);
//						Toast.makeText(ListaNarudzbinaActivity.this,""+nar.getUkupanIznos(),Toast.LENGTH_LONG).show();
						Intent intent =  new Intent(ListaNarudzbinaActivity.this,IzmenaNarudzbineActivity.class);
						intent.putExtra("narudzbina",nar);
						startActivity(intent);
					}
				});
			}
		}
	}


	private class NarudzbineAdapter extends ArrayAdapter<Narudzbina> {
		private List<Narudzbina> listaNarudzbina;
		private Context context;
		private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		public NarudzbineAdapter(Context context, List<Narudzbina> listaNarudzbina) {
			super(context, R.layout.single_row, listaNarudzbina);
			this.listaNarudzbina = listaNarudzbina;
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View row = layoutInflater.inflate(R.layout.single_row, parent,
					false);
			ImageView slika = (ImageView) row.findViewById(R.id.slika);
			TextDrawable drawable = TextDrawable.builder()
					.buildRound("" + listaNarudzbina.get(position).getBrojStola(), Color.parseColor("#FF5722"));
			slika.setImageDrawable(drawable);
			TextView tvLarge = (TextView) row.findViewById(R.id.largeText);
			TextView tvSmall = (TextView) row.findViewById(R.id.smallText);
			tvLarge.setText(sdf.format(listaNarudzbina.get(position).getDatumNarudzbine()));
			tvSmall.setText(String.valueOf("Iznos : "+listaNarudzbina.get(position).getUkupanIznos()));

			return row;
		}

	}
}
