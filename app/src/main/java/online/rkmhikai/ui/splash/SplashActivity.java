package online.rkmhikai.ui.splash;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import online.rkmhikai.MainActivity;
import online.rkmhikai.R;
import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.ui.authentication.LoginRegistrationActivity;
import online.rkmhikai.ui.dashboard.DashBoard;


public class SplashActivity extends AppCompatActivity {
    private static int Splash_screen = 3200;
    boolean connected = false;

    Animation topAnim, bottomAnim;
    ImageView image;
    TextView txtDesc, txtHikai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Making the activity full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(Build.VERSION.SDK_INT>21){

        }
        setContentView(R.layout.activity_splash);


        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        txtHikai = findViewById(R.id.txtHikai);
        image = findViewById(R.id.logo);
        txtDesc = findViewById(R.id.txtDesc);


        txtHikai.setAnimation(topAnim);
        image.setAnimation(topAnim);
        txtDesc.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              login();
            }
        }, Splash_screen);
    }

    public void login() {
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {

            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                //we are connected to a network
                Intent intent = new Intent(SplashActivity.this,LoadActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
               finish();
            }



        } else {
            Intent intent = new Intent(SplashActivity.this, DashBoard.class);
            startActivity(intent);
            finish();
        }
        finish();
    }

}
