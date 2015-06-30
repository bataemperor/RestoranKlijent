package restoran.klijent;


import java.util.ArrayList;


import domen.StavkaNarudzbine;
import restoran.klijent.fragment.FragmentDesert;
import restoran.klijent.fragment.FragmentDorucak;
import restoran.klijent.fragment.FragmentGlavnoJelo;
import restoran.klijent.fragment.FragmentPice;
import restoran.klijent.fragment.FragmentPredjelo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.activity.R;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class ListaProizvodaActivity extends AppCompatActivity {
    ViewPager viewPager;
    public static ArrayList<StavkaNarudzbine> listaStavki = new ArrayList<StavkaNarudzbine>();

    private String mActivityTitle;

    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_proizvoda);
        viewPager = (ViewPager) findViewById(R.id.pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new AdapterProizvodi(fragmentManager));
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
                                    intent = new Intent(ListaProizvodaActivity.this,ListaProizvodaActivity.class);
                                    break;
                                case 1:
                                    intent = new Intent(ListaProizvodaActivity.this,ListaNarudzbinaActivity.class);
                                    break;
                                case 3:
                                    intent = new Intent(ListaProizvodaActivity.this,SettingsActivity.class);
                                    break;
                                case 4:
                                    intent = new Intent(ListaProizvodaActivity.this,LoginActivity.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nova_narudzbina, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if  (id == android.R.id.home){
            if (result.isDrawerOpen()){
                result.closeDrawer();
                getSupportActionBar().setTitle(mActivityTitle);
            }else {
                result.openDrawer();
                getSupportActionBar().setTitle("Navigacija");
            }

            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, NovaNarudzbinaActivity.class);
            intent.putExtra("listaStavki", listaStavki);
            startActivity(intent);
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
    private class AdapterProizvodi extends FragmentPagerAdapter {

        public AdapterProizvodi(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            switch (i) {
                case 0:
                    fragment = new FragmentDorucak();
                    break;
                case 1:
                    fragment = new FragmentPice();
                    break;
                case 2:
                    fragment = new FragmentPredjelo();
                    break;
                case 3:
                    fragment = new FragmentGlavnoJelo();
                    break;
                case 4:
                    fragment = new FragmentDesert();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            switch (position) {
                case 0:
                    title = "Dorucak";
                    break;
                case 1:
                    title = "Pice";
                    break;
                case 2:
                    title = "Predjelo";
                    break;
                case 3:
                    title = "Glavno jelo";
                    break;
                case 4:
                    title = "Desert";
                    break;
            }
            return title;
        }

    }


}
