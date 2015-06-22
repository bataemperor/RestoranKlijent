package dijalog.stavka;

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

public class DijalogStavkaIzmena extends DialogFragment implements View.OnClickListener{

    Button dodajKolicinu, oduzmiKolicinu, dodajStavku, odustani;
    TextView tvProizvod;
    EditText etNapomena;
    private Proizvod proizvod;
    private StavkaNarudzbine stavkaNarudzbine;
    int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dijalog_stavka_izmena, null);
//        stavkaNarudzbine = new StavkaNarudzbine();
//        stavkaNarudzbine.setStavka(proizvod);
//        stavkaNarudzbine.setKolicina(1);
        tvProizvod = (TextView) view.findViewById(R.id.textView_proizvodIzmena);
        etNapomena = (EditText) view.findViewById(R.id.editText_napomenaIzmena);
        dodajKolicinu = (Button) view.findViewById(R.id.button_dodaj_kolicinuIzmena);
        dodajKolicinu.setOnClickListener(this);
        oduzmiKolicinu = (Button) view
                .findViewById(R.id.button_oduzmi_kolicinuIzmena);
        oduzmiKolicinu.setOnClickListener(this);
        dodajStavku = (Button) view.findViewById(R.id.button_dodaj_stavkuIzmena);
        dodajStavku.setOnClickListener(this);
        odustani = (Button) view.findViewById(R.id.button_odustaniIzmena);
        odustani.setOnClickListener(this);
        getDialog().setTitle("" + stavkaNarudzbine.getProizvod().getNazivProizvoda());
        etNapomena.setText(stavkaNarudzbine.getNapomena());
        tvProizvod.setText(String.valueOf(stavkaNarudzbine.getKolicina()));
        return view;
    }

    public static DijalogStavkaIzmena newInstace(StavkaNarudzbine stavka,int position) {
        DijalogStavkaIzmena dijalogStavkaIzmena = new DijalogStavkaIzmena();
        dijalogStavkaIzmena.setStavka(stavka,position);

        return dijalogStavkaIzmena;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_dodaj_kolicinuIzmena:
                stavkaNarudzbine.setKolicina(stavkaNarudzbine.getKolicina() + 1);
                tvProizvod.setText(""+stavkaNarudzbine.getKolicina());
                break;
            case R.id.button_oduzmi_kolicinuIzmena:
                if (Integer.parseInt(tvProizvod.getText().toString())>1) {
                    stavkaNarudzbine.setKolicina(stavkaNarudzbine.getKolicina() - 1);
                    tvProizvod.setText(""+stavkaNarudzbine.getKolicina());
                }
                break;
            case R.id.button_dodaj_stavkuIzmena:
                stavkaNarudzbine.setNapomena(etNapomena.getText().toString());
                stavkaNarudzbine.setIznos(stavkaNarudzbine.getProizvod().getCenaProizvoda() * stavkaNarudzbine.getKolicina());
                ListaProizvodaActivity.listaStavki.remove(position);
                ListaProizvodaActivity.listaStavki.add(stavkaNarudzbine);
                dismiss();
                break;
            case R.id.button_odustani:
                dismiss();
                break;
        }

    }

    public void setStavka(StavkaNarudzbine stavka,int position) {
        this.stavkaNarudzbine = stavka;
        this.position=position;
    }
}
