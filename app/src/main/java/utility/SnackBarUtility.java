package utility;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by bataemperor on 10.6.15..
 */
public class SnackBarUtility {
    public static void prikaziSnackBar(View view){
        Snackbar snackbar = Snackbar.make(view,"Neuspesna konekcija",Snackbar.LENGTH_SHORT);
        snackbar.setAction("Change IP", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        snackbar.setActionTextColor(Color.parseColor("#009688"));
        snackbar.show();
    }
}
