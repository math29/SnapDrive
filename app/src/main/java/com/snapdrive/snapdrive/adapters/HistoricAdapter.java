package com.snapdrive.snapdrive.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.snapdrive.snapdrive.Historic_viewer;
import com.snapdrive.snapdrive.R;
import com.snapdrive.snapdrive.SnapMedia;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by crocus on 07/04/15.
 */
public class HistoricAdapter extends BaseAdapter{
    Context mCtx;
    List<SnapMedia> videoPaths;
    SwipeRefreshLayout swipe;
    LayoutInflater inflater;

    public HistoricAdapter(Context context){
        mCtx = context;
    }

    public HistoricAdapter(Activity activity,Context context, List<SnapMedia> videos, LayoutInflater inflate, SwipeRefreshLayout swipe) {
        videoPaths = videos;
        this.swipe = swipe;
        inflater = inflate;
        mCtx = context;
    }

    @Override
    public int getCount() {
        return videoPaths.size();
    }

    @Override
    public Object getItem(int location) {
        return videoPaths.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.historic_layout, null);


        TextView tv = (TextView)convertView.findViewById(R.id.videoName);
        TextView sizeTv = (TextView)convertView.findViewById(R.id.size);
        TextView dateTv = (TextView)convertView.findViewById(R.id.date);

        ImageButton imgPlay = (ImageButton)convertView.findViewById(R.id.playButton);
        ImageButton imgShare = (ImageButton)convertView.findViewById(R.id.shareButton);
        ImageButton imgDelete = (ImageButton)convertView.findViewById(R.id.deleteButton);

        ImageView pictureView = (ImageView)convertView.findViewById(R.id.pictureView);
        ImageView playImg = (ImageView)convertView.findViewById(R.id.play);
        if(videoPaths.get(position).get_type().equals("picture")){
            pictureView.setImageBitmap(BitmapFactory.decodeFile(videoPaths.get(position).get_data()));
            pictureView.setVisibility(View.VISIBLE);
            imgPlay.setVisibility(View.GONE);
            playImg.setImageResource(android.R.drawable.ic_menu_camera);
        }else{
            playImg.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        final String name = videoPaths.get(position).get_displayName();
        sizeTv.setText(videoPaths.get(position).get_size());
        tv.setText(name);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = new Date(videoPaths.get(position).get_date());

        dateTv.setText("  "+d.toLocaleString());

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, Historic_viewer.class);
                intent.putExtra("videofilename", videoPaths.get(position).get_data());
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

    public void updateAdapter(List<SnapMedia> items){
        videoPaths = items;
        swipe.setRefreshing(false);
        notifyDataSetChanged();
    }
}
