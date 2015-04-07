package com.snapdrive.snapdrive;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import com.snapdrive.snapdrive.adapters.HistoricAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mathieu on 05/04/2015.
 */
public class Historic extends ActionBarActivity{
    private Cursor videocursor;
    private Cursor imagecursor;
    private int video_column_index;
    ListView videolist;
    int count;
    Context context;
    HistoricAdapter hadapter;
    private SwipeRefreshLayout swipeLayout;
    private List<SnapMedia> medias;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historic);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();
        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);

        medias = new ArrayList<SnapMedia>();
        initCursor();
        hadapter = new HistoricAdapter(this, getApplicationContext(),medias,null, swipeLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               refresh();
            }
        });
        videolist = (ListView) findViewById(R.id.PhoneVideoList);
        videolist.setAdapter(hadapter);
        //init_phone_video_grid();
    }


    private void initCursor(){
        medias = new ArrayList<SnapMedia>();
        System.gc();
        String[] proj = { MediaStore.MediaColumns.DATA,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_MODIFIED};
        String selection=MediaStore.Files.FileColumns.DATA +" like?";
        String[] selectionArgs=new String[]{"%SnapDrive%"};
        videocursor = managedQuery(MediaStore.Files.getContentUri("external"),
                proj, selection,selectionArgs, MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC");
        videocursor.moveToFirst();
        while(!videocursor.isAfterLast()){
            if(videocursor.getString(videocursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))!=null) {
                if (videocursor.getString(videocursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)).indexOf(".jpg") != -1 ||
                        videocursor.getString(videocursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)).indexOf(".mp4") != -1) {
                    String displayName = videocursor.getString(videocursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME));
                    long date = videocursor.getLong(videocursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED));
                    String size = videocursor.getString(videocursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
                    String data = videocursor.getString(videocursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                    if (videocursor.getString(videocursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)).indexOf(".jpg") != -1) {
                        SnapMedia m = new SnapMedia("picture", displayName, date, size, data);
                        medias.add(m);
                    } else {
                        SnapMedia m = new SnapMedia("video", displayName, date, size, data);
                        medias.add(m);
                    }
                } else {

                }
            }
            videocursor.moveToNext();
        }
    }


    private void refresh(){
        initCursor();
        hadapter.updateAdapter(medias);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.historic_activity_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }


}
