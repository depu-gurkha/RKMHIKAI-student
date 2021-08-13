package online.rkmhikai.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.config.URLs;
import online.rkmhikai.ui.authentication.LoginRegistrationActivity;
import online.rkmhikai.R;

public class DashBoard extends AppCompatActivity {

    Toolbar myToolbar;
    ImageView ivLanding;
    CardView cardIv;
    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String,List<String>> listItem;
    MainAdapter mainAdapter;
    Button btnClassroom,btnEnrol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        myToolbar=findViewById(R.id.customToolbarr);
        ivLanding=findViewById(R.id.ivLanding);
//        expandableListView=findViewById(R.id.expandableList);
        btnClassroom=findViewById(R.id.btnClassroom);
        btnEnrol=findViewById(R.id.btnEnroll);
        listGroup=new ArrayList<>();
        listItem=new HashMap<>();

        mainAdapter=new MainAdapter(this,listGroup,listItem);
//        expandableListView.setAdapter(mainAdapter);
        initListData();
        cardIv=findViewById(R.id.cardIv);
        cardIv.setBackgroundColor(Color.TRANSPARENT);
        Glide.with(this).load("https://rkmhikai.online/public/assets/img/student.jpg").into(ivLanding);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        btnClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashBoard.this, LoginRegistrationActivity.class);
                intent.putExtra("ComingFrom",0);
                startActivity(intent);

            }
        });
        btnEnrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashBoard.this, LoginRegistrationActivity.class);
                intent.putExtra("ComingFrom",1);
                startActivity(intent);
            }
        });

        if(SharedPrefManager.getInstance(getApplicationContext()).getServerAddress().equals("")){
            SharedPrefManager.getInstance(getApplicationContext()).setServerAddress("https://classroom.chotoly.com");
        }
        //Toast.makeText(this, SharedPrefManager.getInstance(getApplication().getApplicationContext()).getServerAddress()+ URLs.contactUsUrl, Toast.LENGTH_SHORT).show();

    }

    private void initListData() {
        listGroup.add(getString(R.string.group1));
        listGroup.add(getString(R.string.group2));
        listGroup.add(getString(R.string.group3));
        listGroup.add(getString(R.string.group4));
        String[] array;
        List<String> list1=new ArrayList<>();
        array=getResources().getStringArray(R.array.group1);
        for(String item : array){
            list1.add(item);
        }
        List<String> list2=new ArrayList<>();
        array=getResources().getStringArray(R.array.group2);
        for(String item : array){
            list2.add(item);
        }
        List<String> list3=new ArrayList<>();
        array=getResources().getStringArray(R.array.group3);
        for(String item : array){
            list3.add(item);
        }

        List<String> list4=new ArrayList<>();
        array=getResources().getStringArray(R.array.group4);
        for(String item : array){
            list4.add(item);
        }
        listItem.put(listGroup.get(0),list1);
        listItem.put(listGroup.get(1),list2);
        listItem.put(listGroup.get(2),list3);
        listItem.put(listGroup.get(3),list4);
        mainAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id){
//            case R.id.item1:
//                Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.item2:
//                Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.item3:
//                Toast.makeText(getApplicationContext(),"Item 3 Selected", Toast.LENGTH_LONG).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}