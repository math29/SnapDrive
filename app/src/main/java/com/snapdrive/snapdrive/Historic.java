package com.snapdrive.snapdrive;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.snapdrive.snapdrive.adapters.HistoricAdapter;

/**
 * Created by Mathieu on 05/04/2015.
 */
public class Historic extends ActionBarActivity{
    private Cursor videocursor;
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
        String[] proj = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_TAKEN};
        String selection=MediaStore.Video.Media.DATA +" like?";
        String[] selectionArgs=new String[]{"%SnapDrive%"};
        /*videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                proj, selection, selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC");*/
        videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                proj, selection,selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC");
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
