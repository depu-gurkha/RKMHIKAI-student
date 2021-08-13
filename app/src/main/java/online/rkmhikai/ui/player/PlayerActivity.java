package online.rkmhikai.ui.player;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.io.File;

import online.rkmhikai.MainActivity;
import online.rkmhikai.R;
import online.rkmhikai.ui.player.nestedlist.ChapterLecture;

public class PlayerActivity extends AppCompatActivity {

    int SubjectId;
    String SubjectTitle="";

    public int getMySubject() {
        return SubjectId;
    }
    public String getMySubjectTitle() {
        return SubjectTitle;
    }

    VideoViewMainFragment videoViewMainFragment;
    ViewPagerAdapter viewPagerAdapter;
    Uri videoUrl;
    long seek = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getSupportActionBar().hide();
        setContentView(R.layout.activity_player);


//        getSupportActionBar().hide();
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();




        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#007bff")));
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        //white color
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back);


        String url1 = "/storage/emulated/0/Android/data/com.part.hikaiplayer/files/Videos/Videos\\101\\Subject\\SU1\\lec1";
        //Change end

        //Toast.makeText(this, url1, Toast.LENGTH_LONG).show();
        Log.i("Path", url1);
        //Linking
        final ViewPager viewPager = findViewById(R.id.view_pager_main);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        Bundle bundle = getIntent().getExtras();

        SubjectId = bundle.getInt("Subject_ID");
        SubjectTitle = bundle.getString("Subject_Title");

        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>"+SubjectTitle+"</font>"));
        //Adapter for tab items
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 2);

        videoViewMainFragment = new VideoViewMainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, videoViewMainFragment).commit();

        //Connect ViewPagerAdapter to Tablayout
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });





        if (bundle != null) {
            if (bundle.getLong("seek") != 0) {
                //vfragment = new VideoFragment();
                seek = bundle.getLong("seek");
                videoUrl = Uri.parse(bundle.getString("path"));
                SubjectId = bundle.getInt("Subject_ID");
                SubjectTitle = bundle.getString("Subject_Title");
                Log.d("PLAYER", "Inside Bundle: " + SubjectId);



                FragmentManager fragmentManager = getSupportFragmentManager();
                final FragmentTransaction t = fragmentManager.beginTransaction();
                final VideoViewMainFragment f = new VideoViewMainFragment();
                Bundle b2 = new Bundle();
                b2.putLong("seek", seek);
                b2.putString("path", videoUrl.toString());
                b2.putInt("Subject_ID", SubjectId);
                b2.putString("Subject_Title", SubjectTitle);

                f.setArguments(b2);
                t.add(R.id.frame_layout_main, f);
                t.commit();
            }
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

       Intent intent=new Intent(this,MainActivity.class);
       startActivity(intent);
        PlayerActivity.this.finish();
    }
    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}