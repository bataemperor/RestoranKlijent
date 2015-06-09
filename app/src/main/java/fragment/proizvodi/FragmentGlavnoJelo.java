package fragment.proizvodi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import komunikacija.Komunikacija;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;
import util.Konstante;

import com.example.klijentandr2.R;

import domen.Proizvod;
import utility.SnackBarUtility;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FragmentGlavnoJelo extends Fragment {
	private List<Proizvod> listaProizvoda;
	ListView listaGlavnoJelo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_glavnojelo, container, false);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new GetGlavnoJeloTask().execute();
		listaGlavnoJelo = (ListView) getView().findViewById(
				R.id.list_glavnojelo);
	}

	private class GetGlavnoJeloTask extends
			AsyncTask<Void, Void, List<Proizvod>> {
		TransferObjekatZahtev zahtev;
		TransferObjekatOdgovor odgovor;
		List<Proizvod> lp;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			listaProizvoda = new ArrayList<Proizvod>();
		}

		@Override
		protected List<Proizvod> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			zahtev = new TransferObjekatZahtev();
			zahtev.setOperacija(Konstante.VRATI_SVE_PROIZVODE);
			try {
				Komunikacija k = new Komunikacija();
				k.posaljiZahtev(zahtev);
				odgovor = k.procitajOdgovor();
				lp = (List<Proizvod>) odgovor.getRezultat();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return lp;
		}

		@Override
		protected void onPostExecute(List<Proizvod> result) {
			if (odgovor == null) {
				SnackBarUtility.prikaziSnackBar((ListView) getView().findViewById(R.id.lista_desert));
			}
			else{

				for (Proizvod proizvod : result) {
					if (proizvod.getTipProizvoda().equalsIgnoreCase("Glavno jelo")) {
						listaProizvoda.add(proizvod);
					}
				}
				ArrayAdapter<Proizvod> adapter = new ArrayAdapter<Proizvod>(
						getActivity(), android.R.layout.simple_list_item_1,
						listaProizvoda);
				listaGlavnoJelo.setAdapter(adapter);
			}
		}
	}
}
