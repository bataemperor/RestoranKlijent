package restoran.klijent;

import android.content.Context;

import android.graphics.Color;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import domen.Narudzbina;
import domen.StavkaNarudzbine;
import restoran.klijent.komunikacija.Komunikacija;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;
import util.Konstante;
import restoran.klijent.utility.Utility;

public class ListaNarudzbinaActivity extends AppCompatActivity {
    private ListView listView;
    private ProgressBar pb;
    private String mActivityTitle;
    private Drawer result = null;
    public Narudzbina nar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_narudzbina);

        listView = (ListView) findViewById(R.id.lista_narudzbina);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        mActivityTitle = getTitle().toString();
        result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
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
                                    intent = new Intent(ListaNarudzbinaActivity.this, ListaProizvodaActivity.class);
                                    break;
                                case 1:
                                    intent = new Intent(ListaNarudzbinaActivity.this, ListaNarudzbinaActivity.class);
                                    break;
                                case 3:
                                    intent = new Intent(ListaNarudzbinaActivity.this, SettingsActivity.class);
                                    break;
                                case 4:
                                    intent = new Intent(ListaNarudzbinaActivity.this, LoginActivity.class);
                                    break;

                            }
                            startActivity(intent);
                        }

                        return false;
                    }
                }).withSelectedItem(1).build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void novaNarudzbina() {
        Intent intent = new Intent(this, ListaProizvodaActivity.class);
        startActivity(intent);
    }

    private class GetNarudzbineTask extends AsyncTask<Void, Void, List<Narudzbina>> {
        TransferObjekatZahtev zahtev;
        TransferObjekatOdgovor odgovor;
        List<Narudzbina> listaNarudzbina;
        NarudzbineAdapter adapter;

        @Override
        protected void onPreExecute() {
            listView.setVisibility(View.GONE);
            pb.setVisibility(View.VISIBLE);
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
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            return listaNarudzbina;
        }

        @Override
        protected void onPostExecute(List<Narudzbina> narudzbinas) {
            pb.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            if (odgovor == null) {
                Utility.prikaziSnackBar((FrameLayout) findViewById(R.id.activity_lista_narudzbina), Utility.SNACKBAR_NEUSPESNA_KONEKCIJA);
            } else {

                adapter = new NarudzbineAdapter(ListaNarudzbinaActivity.this, listaNarudzbina);
//				adapter = new ArrayAdapter<Narudzbina>(ListaNarudzbinaActivity.this, android.R.layout.simple_list_item_1, listaNarudzbina);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        nar = adapter.getItem(position);
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        StringBuilder stringBuilderStavke = new StringBuilder();

                        MaterialDialog md = new MaterialDialog.Builder(ListaNarudzbinaActivity.this)
                                .title("Broj stola : " + nar.getBrojStola())
                                .content(stringBuilderStavke + "\nUkupan iznos : " + nar.getUkupanIznos() + "\nVreme : " + sdf.format(nar.getDatumNarudzbine()))
                                .positiveText("Naplati")
                                .negativeText("Izmeni")
                                .neutralText("Obrisi")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        super.onPositive(dialog);
                                        nar.setStatus("Placena");
                                        new NaplatiNarudzbinuTask().execute();
                                        new GetNarudzbineTask().execute();
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                        super.onNegative(dialog);
                                        Intent intent = new Intent(ListaNarudzbinaActivity.this, IzmenaNarudzbineActivity.class);
                                        intent.putExtra("narudzbina", nar);
                                        startActivity(intent);

                                    }

                                    @Override
                                    public void onNeutral(MaterialDialog dialog) {
                                        super.onNeutral(dialog);
                                        new ObrisiNarudzbinuTask().execute();
                                        new GetNarudzbineTask().execute();
                                    }
                                })
                                .show();
                        for (StavkaNarudzbine stavka : nar.getListaStavki()) {
                            stringBuilderStavke.append(stavka.getProizvod().getNazivProizvoda() + " : " + stavka.getKolicina() + "\n");
                        }
                        StringBuilder linija = new StringBuilder();
                        String s = "Ukupan iznos : " + nar.getUkupanIznos();
                        for (int i = 2; i < s.length(); i++) {
                            linija.append("--");
                        }
                        md.setContent(stringBuilderStavke + linija.toString() + "\nUkupan iznos : " + nar.getUkupanIznos() + "\nVreme : " + sdf.format(nar.getDatumNarudzbine()));
                        md.show();

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
            tvSmall.setText(String.valueOf("Iznos : " + listaNarudzbina.get(position).getUkupanIznos()));

            return row;
        }

    }

    private class NaplatiNarudzbinuTask extends AsyncTask<Void, Void, Void> {

        TransferObjekatOdgovor toOdgovor;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Komunikacija k = new Komunikacija();
                TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
                toZahtev.setOperacija(Konstante.NAPLATI_NARUDZBINU);
                toZahtev.setParametar(nar);
                k.posaljiZahtev(toZahtev);
                toOdgovor = k.procitajOdgovor();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(ListaNarudzbinaActivity.this, toOdgovor.getOdgovor(), Toast.LENGTH_SHORT).show();
        }
    }
    private class ObrisiNarudzbinuTask extends AsyncTask<Void,Void,Void>{
        TransferObjekatOdgovor toOdgovor;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Komunikacija k = new Komunikacija();
                TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
                toZahtev.setOperacija(Konstante.OBRISI_NARUDZBINU);
                toZahtev.setParametar(nar);
                k.posaljiZahtev(toZahtev);
                toOdgovor = k.procitajOdgovor();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(ListaNarudzbinaActivity.this, toOdgovor.getOdgovor(), Toast.LENGTH_SHORT).show();
        }
    }

}
