package online.rkmhikai.ui.splash;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import online.rkmhikai.MainActivity;
import online.rkmhikai.R;
import online.rkmhikai.Room.MyViewModel;
import online.rkmhikai.Room.Repository;
public class LoadActivity extends AppCompatActivity {
    ProgressBar progressBar;
    Repository repository;
    MyViewModel myViewModel;

    ExecutorService executorService= Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        repository=new Repository(getApplication(),this);
        myViewModel=new MyViewModel(getApplication(),this);
        progressBar=findViewById(R.id.load_progress);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

            load();





    }
    public synchronized void load(){

            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    progressBar.setVisibility(View.VISIBLE);
                    repository.deleteAllsubject();
                    repository.deleteAllNotofication();
                    myViewModel.getVolleyDetails();
                    myViewModel.getNotification();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            });
        }

}