package online.rkmhikai.ui.player;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import online.rkmhikai.R;

public class VideoViewMainFragment extends Fragment {

    PlayerView playerView;
    ProgressBar progressBar;
    ImageView btnFullscreen;
    int subjectId;
    String subjectTitle="";

    public static SimpleExoPlayer simpleExoPlayer;


    String videoUrl;
    long seek=0;



    public VideoViewMainFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_video_view_main, container, false);

        //Initialization
        playerView = v.findViewById(R.id.player_view);
        progressBar = v.findViewById(R.id.progress_bar);
        btnFullscreen = v.findViewById(R.id.bt_fullscreen);

        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            videoUrl = bundle.getString("path");
            seek = bundle.getLong("seek");
            subjectId=bundle.getInt("Subject_ID");
            subjectTitle=bundle.getString("Subject_Title");
            Log.d("FULL", "onCreate: "+subjectId);
            Log.d("FULLSUB", "onCreate: "+subjectTitle);
            //Log.i("SAURABH",videoUrl);


            if (simpleExoPlayer != null) {
                Log.i("SAURABH","NEW LOG");

                simpleExoPlayer.release();
           }

            iniExoplayer(videoUrl,seek);

        }
        return v;
    }

    //To Initialise Simple Exoplayer
    public void iniExoplayer(String url,long seek) {


        //Stop any previous video on clicking new video
        if (simpleExoPlayer != null) {

            simpleExoPlayer.stop();

        }

//        Initialize load control
        LoadControl loadControl = new DefaultLoadControl();
        //Initialize band width meter
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        //Initialize track selector
        TrackSelector trackSelector = new DefaultTrackSelector(
                new AdaptiveTrackSelection.Factory(bandwidthMeter)
        );

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        playerView.setPlayer(simpleExoPlayer);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(),"appname"));
        MediaSource videosource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(url));
        simpleExoPlayer.prepare(videosource);

            simpleExoPlayer.seekTo(seek-2);

        simpleExoPlayer.setPlayWhenReady(true);

        simpleExoPlayer.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                super.onPlayerStateChanged(playWhenReady, playbackState);

                //Check condition
                if (playbackState == Player.STATE_BUFFERING) {
                    //Show progress bar
                    progressBar.setVisibility(View.VISIBLE);
                } else if (playbackState == Player.STATE_READY) {
                    //hide progress bar
                    progressBar.setVisibility(View.GONE);
                }
            }
        });



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerView= view.findViewById(R.id.player_view);
        btnFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), Fullscreen.class);
                intent.putExtra("seek",simpleExoPlayer.getCurrentPosition());
                intent.putExtra("path",videoUrl.toString());
                intent.putExtra("Subject_ID",subjectId);
                intent.putExtra("Subject_Title",subjectTitle);
                simpleExoPlayer.release();
                startActivity(intent);
                getActivity().finish();


            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
       if(simpleExoPlayer!=null)
           simpleExoPlayer.release();
    }

    @Override
    public void onPause() {
        super.onPause();
//        simpleExoPlayer.release();
    }



}