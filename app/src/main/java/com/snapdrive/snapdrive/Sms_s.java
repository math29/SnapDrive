package com.snapdrive.snapdrive;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by Yoann on 31/03/2015.
 */
public class Sms_s {

    private String _number;
    private String _message;
    private Context mCtx;

    public Sms_s(Context context){
        mCtx = context;
    }

    public Sms_s(Context context, String number, String message){
        mCtx = context;
        _number = number;
        _message = message;
    }

    public String get_number() {
        return _number;
    }

    public void set_number(String number) {
        _number = number;
    }

    public String get_message() {
        return _message;
    }

    public void set_message(String message) {
        _message = _message;
    }

    public String getContactName(){
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(_number));
        String projection[] = new String[]{ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = mCtx.getContentResolver().query(uri,
                projection,
                null,
                null,
                null);
        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }else{
            return _number;
        }
    }
}
