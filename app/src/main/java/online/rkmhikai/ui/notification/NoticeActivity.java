package online.rkmhikai.ui.notification;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import online.rkmhikai.R;

public class NoticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //hide the default actionbar
        getSupportActionBar().hide();

        //ReceiveData
        String title = getIntent().getExtras().getString("notice_title");
        String text = getIntent().getExtras().getString("notice_text");
        String img = getIntent().getExtras().getString("notice_img");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        //ini views
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        

        // Setting Collapsing Toolbar Layout Extension Colors
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#ffffff"));
        // Set Collapsing Toolbar Layout's shrinkage color
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));

        //TextView tvTitle = findViewById(R.id.aa_n_title);
        TextView tvText = findViewById(R.id.aa_n_text);
        ImageView iv_Image = findViewById(R.id.aa_thumbnail);
        WebView wvText = findViewById(R.id.wv_text);





        collapsingToolbarLayout.setTitle(title);

        //Setting values to each view
        //tvTitle.setText(title);
        //tvText.setText(text);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvText.setText(Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvText.setText(Html.fromHtml(text));
        }
        tvText.setMovementMethod(LinkMovementMethod.getInstance());

        WebSettings webSettings = wvText.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wvText.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wvText.loadData(text,"text/html","UTF-8");



//        if(img.equalsIgnoreCase(""))
//        {
//
//            Log.i("ABC", "onCreate: Not Happening");
//            Log.i("ABC", "onCreate: "+img);
//        }else {
//            //Setting Image
//            Glide.with(this).load(img).into(iv_Image);
//        }
    }
}