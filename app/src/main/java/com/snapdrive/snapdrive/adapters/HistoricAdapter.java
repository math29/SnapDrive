package com.snapdrive.snapdrive.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.session.MediaController;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.VideoView;

import com.snapdrive.snapdrive.Historic_viewer;
import com.snapdrive.snapdrive.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by crocus on 07/04/15.
 */
public class HistoricAdapter extends BaseAdapter{
    Context mCtx;
    Cursor videoPaths;
    SwipeRefreshLayout swipe;
    LayoutInflater inflater;

    public HistoricAdapter(Context context){
        mCtx = context;
    }

    public HistoricAdapter(Activity activity,Context context, Cursor videos, LayoutInflater inflate, SwipeRefreshLayout swipe) {
        videoPaths = videos;
        this.swipe = swipe;
        inflater = inflate;
        mCtx = context;
    }

    @Override
    public int getCount() {
        return videoPaths.getCount();
    }

    @Override
    public Object getItem(int location) {
        return videoPaths.moveToPosition(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        videoPaths.moveToPosition(position);
        if (inflater == null)
            inflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.historic_layout, null);


        TextView tv = (TextView)convertView.findViewById(R.id.videoName);
        TextView dateTv = (TextView)convertView.findViewById(R.id.date);

        final String name = videoPaths.getString(videoPaths.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
        tv.setText(name);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = new Date(Long.parseLong(videoPaths.getString(videoPaths.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN))));

        dateTv.setText("  "+d.toLocaleString());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, Historic_viewer.class);
                intent.putExtra("videofilename", videoPaths.getString(videoPaths.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);
            }
        });
        /*try {
            //d = f.parse();

            long milliseconds = d.getTime();
            dateTv.setText(""+d.toLocaleString());

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/




        android.widget.MediaController mediaController = new android.widget.MediaController(mCtx);


        return convertView;
    }

    public void updateAdapter(Cursor items){
        videoPaths = items;
        swipe.setRefreshing(false);
        notifyDataSetChanged();
    }
}
