package restoran.klijent.dialog;

import restoran.klijent.IzmenaNarudzbineActivity;
import restoran.klijent.ListaProizvodaActivity;
import com.example.activity.R;


import domen.StavkaNarudzbine;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class DialogStavkaUpdate extends DialogFragment implements View.OnClickListener {



    Button dodajKolicinu, oduzmiKolicinu, dodajStavku, odustani;
    TextView tvProizvod;
    EditText etNapomena;
    private StavkaNarudzbine stavkaNarudzbine;
    int position;
    CallbackDialog cb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_stavka_update, null);
        tvProizvod = (TextView) view.findViewById(R.id.textView_proizvodUpdate);
        etNapomena = (EditText) view.findViewById(R.id.editText_napomenaUpdate);
        dodajKolicinu = (Button) view.findViewById(R.id.button_dodaj_kolicinuUpdate);
        dodajKolicinu.setOnClickListener(this);
        oduzmiKolicinu = (Button) view
                .findViewById(R.id.button_oduzmi_kolicinuUpdate);
        oduzmiKolicinu.setOnClickListener(this);
        dodajStavku = (Button) view.findViewById(R.id.button_dodaj_stavkuUpdate);
        dodajStavku.setOnClickListener(this);
        odustani = (Button) view.findViewById(R.id.button_obrisiUpdate);
        odustani.setOnClickListener(this);
        getDialog().setTitle("" + stavkaNarudzbine.getProizvod().getNazivProizvoda());
        etNapomena.setText(stavkaNarudzbine.getNapomena());
        tvProizvod.setText(String.valueOf(stavkaNarudzbine.getKolicina()));
        return view;
    }

    public static DialogStavkaUpdate newInstace(StavkaNarudzbine stavka,int position,CallbackDialog cb) {
        DialogStavkaUpdate dijalogStavkaUpdate = new DialogStavkaUpdate();
        dijalogStavkaUpdate.setStavka(stavka, position, cb);

        return dijalogStavkaUpdate;
    }
    public interface CallbackDialog{
        void callback();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_dodaj_kolicinuUpdate:
                stavkaNarudzbine.setKolicina(stavkaNarudzbine.getKolicina() + 1);
                tvProizvod.setText(""+stavkaNarudzbine.getKolicina());
                break;
            case R.id.button_oduzmi_kolicinuUpdate:
                if (Integer.parseInt(tvProizvod.getText().toString())>1) {
                    stavkaNarudzbine.setKolicina(stavkaNarudzbine.getKolicina() - 1);
                    tvProizvod.setText(""+stavkaNarudzbine.getKolicina());
                }
                break;
            case R.id.button_dodaj_stavkuUpdate:
                stavkaNarudzbine.setNapomena(etNapomena.getText().toString());
                stavkaNarudzbine.setIznos(stavkaNarudzbine.getProizvod().getCenaProizvoda() * stavkaNarudzbine.getKolicina());
//                ListaProizvodaActivity.listaStavki.remove(position);
//                ListaProizvodaActivity.listaStavki.add(stavkaNarudzbine);
                IzmenaNarudzbineActivity.listaStavkiNarudzbine.set(position,stavkaNarudzbine);
                dismiss();
                cb.callback();

                break;
            case R.id.button_obrisiUpdate:
                IzmenaNarudzbineActivity.listaStavkiNarudzbine.remove(position);
                cb.callback();
                dismiss();
                break;
        }

    }

    public void setStavka(StavkaNarudzbine stavka,int position,CallbackDialog cb) {
        this.stavkaNarudzbine = stavka;
        this.position=position;
        this.cb = cb;
    }
}
