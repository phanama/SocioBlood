package com.example.yudiandrean.socioblood;

/**
 * Created by yudiandrean on 5/10/2015.
 */


        import android.app.Fragment;
        import android.view.View;
        import android.os.Bundle;

        import android.os.Handler;
        import android.view.LayoutInflater;
        import android.view.ViewGroup;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;

public class SplashActivity extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2500;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_fragment);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}

//public class SplashFragment extends Fragment {
//
//
//
//    @Override
//    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
//
////        new Handler().postDelayed(new Runnable() {
////                                      @Override
////                                      public void run() {
////                                /* Create an Intent that will start the Menu-Activity. */
////                                          inflater.inflate(R.layout.splash_fragment, container, false);
////                                      }
////                                  },
////                SPLASH_DISPLAY_LENGHT);
//
//        return inflater.inflate(R.layout.splash_fragment, container, false);
//    }
//}