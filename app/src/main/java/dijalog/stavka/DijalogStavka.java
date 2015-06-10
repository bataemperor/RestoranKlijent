package dijalog.stavka;

import java.util.ArrayList;

import com.example.klijentandr2.ListaProizvodaActivity;
import com.example.klijentandr2.R;

import domen.Proizvod;
import domen.StavkaNarudzbine;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DijalogStavka extends DialogFragment implements View.OnClickListener {
	Button dodajKolicinu, oduzmiKolicinu, dodajStavku, odustani;
	TextView tvProizvod;
	EditText etNapomena;
	private ArrayList<StavkaNarudzbine> listaStavki;
	private Proizvod proizvod;
	private StavkaNarudzbine stavkaNarudzbine;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dijalog_stavka, null);
		stavkaNarudzbine = new StavkaNarudzbine();
		stavkaNarudzbine.setProizvod(proizvod);
		stavkaNarudzbine.setKolicina(1);
		tvProizvod = (TextView) view.findViewById(R.id.textView_proizvod);
		etNapomena = (EditText) view.findViewById(R.id.editText_napomena);
		dodajKolicinu = (Button) view.findViewById(R.id.button_dodaj_kolicinu);
		dodajKolicinu.setOnClickListener(this);
		oduzmiKolicinu = (Button) view
				.findViewById(R.id.button_oduzmi_kolicinu);
		oduzmiKolicinu.setOnClickListener(this);
		dodajStavku = (Button) view.findViewById(R.id.button_dodaj_stavku);
		dodajStavku.setOnClickListener(this);
		odustani = (Button) view.findViewById(R.id.button_odustani);
		odustani.setOnClickListener(this);
		getDialog().setTitle(""+stavkaNarudzbine.getProizvod().getNazivProizvoda());

		return view;
	}

	public static DijalogStavka newInstace(Proizvod p) {
		DijalogStavka dijalogStavka = new DijalogStavka();
		dijalogStavka.setProizvod(p);
		return dijalogStavka;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_dodaj_kolicinu:
			stavkaNarudzbine.setKolicina(stavkaNarudzbine.getKolicina() + 1);
			tvProizvod.setText(""+stavkaNarudzbine.getKolicina());
			break;
		case R.id.button_oduzmi_kolicinu:
			if (Integer.parseInt(tvProizvod.getText().toString())>1) {
				stavkaNarudzbine.setKolicina(stavkaNarudzbine.getKolicina() - 1);
				tvProizvod.setText(""+stavkaNarudzbine.getKolicina());
			}
			break;
		case R.id.button_dodaj_stavku:
			stavkaNarudzbine.setNapomena(etNapomena.getText().toString());
			stavkaNarudzbine.setIznos(stavkaNarudzbine.getProizvod().getCenaProizvoda()*stavkaNarudzbine.getKolicina());
			ListaProizvodaActivity.listaStavki.add(stavkaNarudzbine);
			dismiss();
			break;
		case R.id.button_odustani:
			dismiss();
			break;
		}	

	}

	public void setProizvod(Proizvod proizvod) {
		this.proizvod = proizvod;
	}

	public Proizvod getProizvod() {
		return proizvod;
	}
}