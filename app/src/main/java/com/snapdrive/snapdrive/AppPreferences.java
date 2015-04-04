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
}
