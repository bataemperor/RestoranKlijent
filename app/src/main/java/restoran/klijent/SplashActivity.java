package restoran.klijent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;


import com.example.activity.R;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class SplashActivity extends Activity {
    private static final int SPLASH_DISPLAY_LENGTH = 3000;
    SmoothProgressBar spb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        spb = (SmoothProgressBar) findViewById(R.id.smooth_progress);

        spb.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                SplashActivity.this.startActivity(mainIntent);

                SplashActivity.this.finish();
                spb.setVisibility(View.INVISIBLE);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
