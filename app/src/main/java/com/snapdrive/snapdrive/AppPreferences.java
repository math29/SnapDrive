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

    /**
     * Constructeur
     * @param context
     */
    public AppPreferences(Context context){
        mCtx = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
    }

    /**
     * do the user want to have his messages read?
     * @return: yes he want, no he doesn't
     */
    public boolean isActivate(){
        return prefs.getBoolean("activated",false);
    }

    /**
     * the user choose wether he want to activate or not
     * @param activation
     */
    public void activate(boolean activation){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("activated",activation);
        edit.commit();
    }

    /**
     * set number of last received SMS
     * @param number
     */
    public void setNumber(String number){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("Number",number);
        edit.commit();
    }

    /**
     * get number of last received sms
     * @return
     */
    public String getNumber(){
        return prefs.getString("Number","N/A");
    }

    /**
     * set video path
     * @param path
     */
    public void setVideoPath(String path){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("videoPath",path);
        edit.commit();
    }


    /**
     * get video path
     * @return
     */
    public String getVideoPath(){
        return prefs.getString("videoPath","N/A");
    }
}
