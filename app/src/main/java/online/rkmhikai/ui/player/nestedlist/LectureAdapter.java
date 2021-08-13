package online.rkmhikai.ui.player.nestedlist;
import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import online.rkmhikai.Assignment;
import online.rkmhikai.MainActivity;
import online.rkmhikai.R;
import online.rkmhikai.ResourceActivity;
import online.rkmhikai.Tables.Content;
import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.ui.MyQuiz.PlayActivity;
import online.rkmhikai.ui.player.PlayerActivity;
import online.rkmhikai.ui.splash.LoadActivity;
import online.rkmhikai.ui.splash.SplashActivity;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.MyLectureHolder> {
    Context mContext;
    private List<Content> contents;

    RecyclerViewClickInterface recyclerViewClickInterface;


    private static final int PERMISSION_STORAGE_CODE = 1000;

    private ProgressDialog pDialog;
    NotificationCompat.Builder builder;
    int PROGRESS_MAX = 100;
    int PROGRESS_CURRENT = 0;

    NotificationManagerCompat notificationManager;

    public LectureAdapter(Context mContext, List<Content> contents, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.mContext = mContext;
        this.contents = contents;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public MyLectureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lecture_item, parent, false);
        return new MyLectureHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyLectureHolder holder, int position) {
       Content lecture =contents.get(position);
        holder.tvSubItemTitle.setText(lecture.getTitle());
        holder.tvType.setText(lecture.getType());
        Log.d("EXDIR", "onBindViewHolder: "+Environment.getExternalStorageDirectory());
//        final File file = new File("/storage/emulated/0"+lecture.getLocalUrl());
        final File file = new File(Environment.getExternalStorageDirectory()+lecture.getLocalUrl());
        if (file.exists()) {
            Log.d("Inside File Exists", file.getName());
            holder.btnDownload.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
            holder.btnDownload.setEnabled(false);
        }
        if(contents.get(position).getType().equals("quiz") || contents.get(position).getType().equals("assignment") || contents.get(position).getType().equals("resources")){
            Log.d("HIDESHOW", "onBindViewHolder: "+contents.get(position).getType());
            holder.btnDownload.setImageResource(R.drawable.ic_baseline_book_24);
            holder.playicon.setImageResource(R.drawable.ic_baseline_book_24);
            holder.btnDownload.setClickable(false);
            holder.btnDownload.setEnabled(false);
            //holder.btnDownload.setVisibility(View.GONE);
        }
        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               if(contents.get(position).getType().equals("quiz")){
                   Intent intent=new Intent(mContext.getApplicationContext(), PlayActivity.class);
                   intent.putExtra("ContentID",contents.get(position).getContentID());
                   intent.putExtra("Title",contents.get(position).getTitle());

                   v.getContext().startActivity(intent);
               }
               else if(contents.get(position).getType().equals("assignment")){

                   Intent intent=new Intent(mContext.getApplicationContext(), Assignment.class);
                   intent.putExtra("link",lecture.getWebUrl());
                   intent.putExtra("loacation",lecture.getLocalUrl());
                   intent.putExtra("fileName",lecture.getTitle());

                   v.getContext().startActivity(intent);
               }
               else if(contents.get(position).getType().equals("resources")){

                   Intent intent=new Intent(mContext.getApplicationContext(), ResourceActivity.class);
                   intent.putExtra("resource_url",lecture.getWebUrl());
                   intent.putExtra("resourceFileName",lecture.getTitle());
                   v.getContext().startActivity(intent);
               }
               else{
                    if(file.exists()){
                       //Play video
                       Toast.makeText(v.getContext(), "Playing: " + lecture.getTitle(), Toast.LENGTH_SHORT).show();
                       Log.d("FILE", "onClick: "+lecture.getLocalUrl());
                       recyclerViewClickInterface.onItemClick(Environment.getExternalStorageDirectory()+lecture.getLocalUrl());
                    }else {
                        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                            Toast.makeText(v.getContext(), "Playing: " + lecture.getTitle(), Toast.LENGTH_SHORT).show();
                            Log.d("FILE", "onClick: "+lecture.getWebUrl());
                            recyclerViewClickInterface.onItemClick(lecture.getWebUrl());
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setMessage("No Internet Connection! Please connect to internet")
                                    .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            mContext.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                                        }
                                    })
                                    .setCancelable(false)
                                    .setNegativeButton("CANCEL", null);
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

               }
            }
        });

        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Downloading ...", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions((Activity) view.getContext(), permissions, PERMISSION_STORAGE_CODE);
                    } else {

                        String file_url = lecture.getWebUrl().replace("/storage/emulated/0/Android/data/online.rkmhikai/files/Videos", SharedPrefManager.getInstance(mContext.getApplicationContext()).getServerAddress()+"/public/media/");
//                        String file_url=Environment.getExternalStorageDirectory()+lecture.getLocalUrl();

                        Log.i("RECEIVE-URL1", lecture.getWebUrl());
//                        Toast.makeText(view.getContext(),receiveUrl, Toast.LENGTH_LONG).show();
                        Log.i("RECEIVE-URL", file_url);
                        new DownloadFileFromURL(view, lecture.getTitle(),holder,file_url).execute(file_url);
//                        holder.btnDownload.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
//                        holder.btnDownload.setEnabled(false);
                    }
                } else {
                    //system OS is less than Marshmellow, perform download
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public class MyLectureHolder extends RecyclerView.ViewHolder {

        TextView tvSubItemTitle,tvType;
        LinearLayout linearLayoutVideo;
        ImageView btnDownload,playicon;
        CardView cvMain;

        public MyLectureHolder(@NonNull View itemView) {
            super(itemView);
            tvSubItemTitle = itemView.findViewById(R.id.tv_lecture_title);
            linearLayoutVideo = itemView.findViewById(R.id.linear_layout_video);
            btnDownload = itemView.findViewById(R.id.btn_download_video);
            tvType=itemView.findViewById(R.id.tv_lecture_type);
            cvMain = itemView.findViewById(R.id.cv_main);
            playicon=itemView.findViewById(R.id.imageView2);
        }
    }

    protected void createDialog(View view, String title) {
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setTitle(title);
        pDialog.setMessage("Downloading... Please wait!");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    protected void notificationProgress(String title) {
        Intent intent = new Intent(mContext, PlayerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        builder = new NotificationCompat.Builder(mContext, "MYNOTIFICATION")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Downloading")
                .setContentText(title)
                // Set the intent that will fire when the user taps the notification
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager = NotificationManagerCompat.from(mContext);

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
        MyLectureHolder holder;
        String file_url;

        private AsyncTask<String, String, String> cancelTask = null;


        DownloadFileFromURL(View view, String title,MyLectureHolder holder,String file_url) {
            this.view = view;
            this.title = title;
            this.holder = holder;
            this.file_url = file_url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cancelTask = this;
            notificationProgress(this.title);
            //createDialog(this.view, this.title);
            pDialog = new ProgressDialog(view.getContext());
            pDialog.setTitle(title);
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

                        File del_Path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/online.rkmhikai/files/Videos/" + file_url.replace(SharedPrefManager.getInstance(view.getContext()).getServerAddress()+"/public/media/", ""));
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

            //Required only if setCancelable is true
//            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialogInterface) {
//                    if(cancelTask!=null){
//                        cancelTask.cancel(true);
//                    }
//                    if (pDialog!=null){
//                        pDialog.dismiss();
//                        Log.d("CANCELTAG", "onCancel: from backpressed");
//                    }
//
//                }
//            });
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
                //filename
                String fname = URLUtil.guessFileName(url.toString(), null, null);
                String path1 = url.toString().replace(fname, "");
                Log.d("PATH", path1);
                Log.d("FileName", fname);
                String internalPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/online.rkmhikai/files/Videos/" + path1.replace(SharedPrefManager.getInstance(view.getContext()).getServerAddress()+"/public/media/", "");
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
                OutputStream output = new FileOutputStream(internalPath + "/" + fname);

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
            notificationManager.cancel(1);
            holder.btnDownload.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
            holder.btnDownload.setEnabled(false);
        }

    }
}
