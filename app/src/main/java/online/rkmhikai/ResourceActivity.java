package online.rkmhikai;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import online.rkmhikai.ui.player.nestedlist.LectureAdapter;

public class ResourceActivity extends AppCompatActivity {

    Button btnDownlaodResource;
    private String resourceUrl,resourceFileName,fileName;

    private ProgressDialog pDialog;

    ActionBar resourceActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);

        btnDownlaodResource =findViewById(R.id.btn_download_resource);

        resourceUrl=getIntent().getStringExtra("resource_url");
        resourceFileName =getIntent().getStringExtra("resourceFileName");

        resourceActionBar = getSupportActionBar();
        // showing the back button in action bar
        resourceActionBar.setDisplayHomeAsUpEnabled(true);
        resourceActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#007bff")));
        //white color
        resourceActionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back);

        btnDownlaodResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("RESOURCE", "onClick: Download");
                //startDownloading(assignmentUrl);
                new DownloadFileFromURL(view, resourceFileName,resourceUrl).execute(resourceUrl);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
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
            pDialog.setTitle(title+" (Resource)");
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ResourceActivity.this);
            builder.setTitle("Download Completed")
                    .setMessage("Please check Download Folder for the Resource: "+title)
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