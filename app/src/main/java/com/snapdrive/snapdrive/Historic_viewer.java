package com.snapdrive.snapdrive;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.VideoView;

/**
 * Created by Mathieu on 06/04/2015.
 */
public class Historic_viewer extends ActionBarActivity {
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private Context context;
    private String filename;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_viewer);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();
        filename = getIntent().getStringExtra("videofilename");

        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        android.widget.MediaController mediaController = new android.widget.MediaController(Historic_viewer.this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(filename);

        videoView.start();
        Log.v("Checkpoint", "On vient la ????????????????????????");
    }
}
