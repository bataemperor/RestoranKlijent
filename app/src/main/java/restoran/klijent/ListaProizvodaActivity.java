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

import com.example.activity.R;


public class ListaProizvodaActivity extends AppCompatActivity {
	ViewPager viewPager;
	public static ArrayList<StavkaNarudzbine> listaStavki =  new ArrayList<StavkaNarudzbine>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_proizvoda);
		viewPager = (ViewPager) findViewById(R.id.pager);
		FragmentManager fragmentManager = getSupportFragmentManager();
		viewPager.setAdapter(new AdapterProizvodi(fragmentManager));
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
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this,NovaNarudzbinaActivity.class);
			intent.putExtra("listaStavki", listaStavki);
			startActivity(intent);

			return true;
		}
		return super.onOptionsItemSelected(item);
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