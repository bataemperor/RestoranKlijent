package utility;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by bataemperor on 10.6.15..
 */
public class SnackBarUtility {
    public static final String NEUSPESNA_KONEKCIJA = "Neuspesna konekcija";
    public static final String NEUSPESAN_LOGIN = "Ne postoji takav konobar";
    public static final String USPESAN_LOGIN = "Uspesno ste se ulogovali";
    public static final String ACTION_CHANGE_IP = "Change IP";


    public static void prikaziSnackBar(View view,String poruka){
        Snackbar snackbar = Snackbar.make(view,poruka,Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
    public static void prikaziSnackBar(View view,String poruka,String action){
        Snackbar snackbar = Snackbar.make(view,poruka,Snackbar.LENGTH_SHORT);
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        snackbar.setActionTextColor(Color.parseColor("#009688"));
        snackbar.show();
    }
}
