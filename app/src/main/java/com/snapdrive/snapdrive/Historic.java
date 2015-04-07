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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historic);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();
        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);


        initCursor();
        hadapter = new HistoricAdapter(this, getApplicationContext(),videocursor,null, swipeLayout);
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
        System.gc();
        String[] proj = { MediaStore.MediaColumns.DATA,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE};
        String selection=MediaStore.Files.FileColumns.DATA +" like?";
        String[] selectionArgs=new String[]{"%SnapDrive%"};
        videocursor = managedQuery(MediaStore.Files.getContentUri("external"),
                proj, selection,selectionArgs, MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC");
    }


    private void refresh(){
        initCursor();
        hadapter.updateAdapter(videocursor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.historic_activity_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }


}
