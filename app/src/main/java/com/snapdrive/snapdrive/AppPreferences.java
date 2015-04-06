package com.snapdrive.snapdrive;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Yoyo on 31/03/2015.
 */
public class AppPreferences {

    SharedPreferences prefs;
    private Context mCtx;

    public AppPreferences(Context context){
        mCtx = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
    }

    public boolean isActivate(){
        return prefs.getBoolean("activated",false);
    }

    public void activate(boolean activation){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("activated",activation);
        edit.commit();
    }

    public void setNumber(String number){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("Number",number);
        edit.commit();
    }

    public String getNumber(){
        return prefs.getString("Number","N/A");
    }

    public void setVideoPath(String path){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("videoPath",path);
        edit.commit();
    }

    public String getVideoPath(){
        return prefs.getString("videoPath","N/A");
    }
}
