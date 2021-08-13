package online.rkmhikai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;

import online.rkmhikai.Room.MyViewModel;
import online.rkmhikai.Room.Repository;
import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.ui.authentication.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    Repository repository;
    MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // FloatingActionButton fab = findViewById(R.id.fab);
        repository = new Repository(getApplication(), this);
        myViewModel = new MyViewModel(getApplication(), this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_contact, R.id.nav_about, R.id.nav_notification,R.id.nav_server_address)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        // This line needs to be after setupWithNavController()
        navigationView.setNavigationItemSelectedListener(this);

        //Getting the nav header componets and setting the user name
        View hView = navigationView.getHeaderView(0);
        User user = SharedPrefManager.getInstance(this).getUser();
        TextView participantName = hView.findViewById(R.id.txtParticipantName);
        TextView participantClass = hView.findViewById(R.id.txtParticipantClass);
        TextView participantId = hView.findViewById(R.id.txtParticipantId);
        ImageView participantProfile = hView.findViewById(R.id.iv_profile_pic);
        participantClass.setText(user.getStandard());
        participantName.setText(user.getName().toUpperCase());
        participantId.setText("Participant ID: " + user.getParticipantId());
        String fileName = URLUtil.guessFileName(SharedPrefManager.getInstance(this).getUser().getProfilePicture(), null, null);
        Log.d("PROFILE", "onCreate: " + fileName);
        File imgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/online.rkmhikai/files/ProfilePic/" + fileName);
        if(imgFile.exists()) {
            Glide.with(getApplicationContext())
                    .load(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/online.rkmhikai/files/ProfilePic/" + fileName)
                    .override(200, 200)
                    .into(participantProfile);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.executePendingTransactions();
//        if (fragmentManager.getBackStackEntryCount() > 1) {
//            //Go back to previous Fragment
//            Log.d("TAG", "onBackPressed: "+fragmentManager.getBackStackEntryCount());
//            fragmentManager.popBackStackImmediate();
//
//        }
//
//
//        else {
//            super.onBackPressed();
//            //Nothing in the back stack, so exit
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
//        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPrefManager.getInstance(this).logout();
                break;
            case R.id.nav_go_to_website:
                Toast.makeText(this, "Opening Website", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(SharedPrefManager.getInstance(getApplicationContext()).getServerAddress()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            default:
                // Trigger the default action of replacing the current
                // screen with the one matching the MenuItem's ID
                NavigationUI.onNavDestinationSelected(item, navController);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}