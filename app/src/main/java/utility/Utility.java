package utility;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.klijentandr2.R;

/**
 * Created by bataemperor on 10.6.15..
 */
public class Utility {
    public static final String SNACKBAR_NEUSPESNA_KONEKCIJA = "Neuspesna konekcija";
    public static final String SNACKBAR_NEUSPESAN_LOGIN = "Ne postoji takav konobar";
    public static final String SNACKBAR_USPESAN_LOGIN = "Uspesno ste se ulogovali";
    public static final String SNACKBAR_ACTION_CHANGE_IP = "Change IP";
    static MaterialDialog md;

//SNACKBAR

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

//DIALOG

//    public static MaterialDialog getProgressDialogMaterial() {
//        md = new MaterialDialog.Builder(this)
//                .title(R.string.dialog_title_login)
//                .content(R.string.dialog_tekst_login)
//                .progress(true, 0).show();
//        return md;
//    }
//
//    public static  void dismissDialogMaterial() {
//        if (md!=null){
//            md.setCancelable(false);
//            md.dismiss();
//        }
//    }

}
