package com.snapdrive.snapdrive;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
    VideoAdapter adaptater;
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
        count = videocursor.getCount();

        hadapter = new HistoricAdapter(this, getApplicationContext(),videocursor,null, swipeLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
                hadapter.updateAdapter(videocursor);
            }
        });
        videolist = (ListView) findViewById(R.id.PhoneVideoList);
        videolist.setAdapter(hadapter);
        //init_phone_video_grid();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.historic_activity_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refresh:
                System.gc();
                String[] proj = { MediaStore.Video.Media._ID,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.DATE_MODIFIED};
                String selection=MediaStore.Video.Media.DATA +" like?";
                String[] selectionArgs=new String[]{"%SnapDrive%"};
        /*videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                proj, selection, selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC");*/
                videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        proj, selection,selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC");
                count = videocursor.getCount();
                adaptater.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Action !", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void init_phone_video_grid() {
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
                count = videocursor.getCount();
        videolist = (ListView) findViewById(R.id.PhoneVideoList);
        adaptater = new VideoAdapter(context);
        videolist.setAdapter(adaptater);
        videolist.setOnItemClickListener(videogridlistener);


    }

    private AdapterView.OnItemClickListener videogridlistener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            System.gc();
            video_column_index = videocursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            videocursor.moveToPosition(position);
            String filename = videocursor.getString(video_column_index);
            Intent intent = new Intent(Historic.this, Historic_viewer.class);
            intent.putExtra("videofilename", filename);
            startActivity(intent);
            //Toast.makeText(getApplicationContext(), filename, Toast.LENGTH_SHORT).show();

            /*MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(context, Uri.fromFile(new File(filename)));
                mediaPlayer.prepare();
            }catch(IOException e){
            Log.e("Error", e.getMessage());
        }
            mediaPlayer.start();*/
        }
    };

    public class VideoAdapter extends BaseAdapter {
        private Context vContext;

        public VideoAdapter(Context c) {
            vContext = c;
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            System.gc();
            TextView tv = new TextView(vContext.getApplicationContext());
            String id = null;
            if (convertView == null) {
                video_column_index = videocursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                videocursor.moveToPosition(position);
                id = videocursor.getString(video_column_index);
                tv.setText(id);
            } else
                tv = (TextView) convertView;
            return tv;
        }
    }
}
