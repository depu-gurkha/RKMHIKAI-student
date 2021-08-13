package online.rkmhikai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.ui.player.PlayerActivity;
import online.rkmhikai.ui.player.nestedlist.LectureAdapter;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Assignment extends AppCompatActivity implements View.OnClickListener{

    ProgressBar progressBar;
    Button btnUpload, btnDownlaod;
    private int STORAGE_PERMISSION_CODE = 1;
    String assignmentUrl,loaclstorage;
    String fileName;
    private ProgressDialog pDialog;
    NotificationCompat.Builder builder;
    int PROGRESS_MAX = 100;
    int PROGRESS_CURRENT = 0;

    NotificationManagerCompat notificationManager;

    ActionBar assignmentActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        //progressBar=findViewById(R.id.pb_assignment);
        btnUpload=findViewById(R.id.btnUpload);
        btnDownlaod =findViewById(R.id.btn_dwnld);
        btnDownlaod.setOnClickListener(this);
        btnUpload.setOnClickListener(this );
        assignmentUrl=getIntent().getStringExtra("link");
        loaclstorage=getIntent().getStringExtra("loacation");
        fileName =getIntent().getStringExtra("fileName");

        assignmentActionBar = getSupportActionBar();
        // showing the back button in action bar
        assignmentActionBar.setDisplayHomeAsUpEnabled(true);
        assignmentActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#007bff")));
        //white color
        assignmentActionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_dwnld:
                Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                //startDownloading(assignmentUrl);
                new DownloadFileFromURL(view, fileName,assignmentUrl).execute(assignmentUrl);
                break;
            case R.id.btnUpload:
                Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(SharedPrefManager.getInstance(getApplicationContext()).getServerAddress()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
//                if (ContextCompat.checkSelfPermission(this,
//                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    CropImage.startPickImageActivity(this);
//                    // Log.d("Crop Image", ).toString());
//
//                } else {
//
//                    requestStoragePermission();
//
//                }
        }
    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Assignment.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Assignment.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(Assignment.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Assignment.this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Assignment.this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
//    private void startDownloading(String url){
//        //get url/text from edit text
//
//        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
//
//        Log.i("TAG", "startDownloading: "+url);
//        Log.i("TAG", "Filename: "+ URLUtil.guessFileName(url,null,null));
//
//        //create download request
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//        //Allow types of network to download
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
//        request.setTitle("Download");
//        request.setDescription("Downloading File...");
//        request.allowScanningByMediaScanner();
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//        //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis()); //get current timestamp as file name
////        request.setDestinationInExternalFilesDir(getApplicationContext(),"/Videos/abc","Hello-"+System.currentTimeMillis()+".jpg");
////        request.setDestinationInExternalFilesDir(getApplicationContext(), String.valueOf(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url,null,null));
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);
//
//
//
//        //get download service and enque file
//        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        manager.enqueue((request));
//    }

    protected void notificationProgress(String title) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        builder = new NotificationCompat.Builder(this, "MYNOTIFICATION")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Downloading")
                .setContentText(title)
                // Set the intent that will fire when the user taps the notification
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager = NotificationManagerCompat.from(this);

        // Issue the initial notification with zero progress

        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, true);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());

    }


    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        View view;
        String title;
        LectureAdapter.MyLectureHolder holder;
        String file_url;

        private AsyncTask<String, String, String> cancelTask = null;


        DownloadFileFromURL(View view, String title, String file_url) {
            this.view = view;
            this.title = title;

            this.file_url = file_url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cancelTask = this;
            //notificationProgress(this.title);
            //createDialog(this.view, this.title);
            pDialog = new ProgressDialog(view.getContext());
            pDialog.setTitle(title+" (Assignment)");
            pDialog.setMessage("Downloading... Please wait!");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.setButton(-1, "CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(view.getContext(), "Download is Cancelled", Toast.LENGTH_SHORT).show();
                    if(cancelTask!=null){
                        cancelTask.cancel(true);

//                        File del_Path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/online.rkmhikai/files/Videos/" + file_url.replace(SharedPrefManager.getInstance(view.getContext()).getServerAddress()+"/public/media/", ""));
                        File del_Path=new File(Environment.DIRECTORY_DOWNLOADS+fileName);
                        if(del_Path.exists()){
                            if (del_Path.delete()) {
                                Log.d("CANCELTAG", "onClick: File deleted");
                            } else {
                                Log.d("CANCELTAG", "onClick: File deleted");
                            }
                        }
                        Log.d("CANCELTAG", "onClick: Deleteing "+del_Path);
                    }
                    if (pDialog!=null){
                        pDialog.dismiss();
                        Log.d("CANCELTAG", "onCancel2: from CANCEL 2 Button");
                    }
                }
            });


            pDialog.show();

        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                String extension ="";
                //filename
                String fname = URLUtil.guessFileName(url.toString(), null, null);
                int index = fname.lastIndexOf('.');
                if(index > 0) {
                    extension = fname.substring(index + 1);
                }
                String path1 = url.toString().replace(fname, "");
                Log.d("PATH1", path1);
                Log.d("FileName", fname);
                //String internalPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/online.rkmhikai/files/Videos/" + path1.replace(SharedPrefManager.getInstance(view.getContext()).getServerAddress()+"/public/media/", "");
                String internalPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                File mydir = new File(internalPath);
                Log.d("PATH", mydir.toString());

                if (mydir.exists()) {
                    Log.d("PATH", "YES");
                } else {
                    mydir.mkdirs();
                    Log.d("PATH", "NO");
                }
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a typical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                OutputStream output = new FileOutputStream(internalPath + "/" + title+"."+extension);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    int percentage = (int) ((total * 100) / lenghtOfFile);
                    publishProgress("" + percentage);


                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();



            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            pDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(Assignment.this);
            builder.setTitle("Download Completed")
                    .setMessage("Please check Download Folder for the Assignment: "+title)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //getApplicationContext().startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    })
                    .setCancelable(false);
            AlertDialog alert = builder.create();
            alert.show();
            //notificationManager.cancel(1);

        }

    }
}