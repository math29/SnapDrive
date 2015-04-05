package com.snapdrive.snapdrive;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Yoyo on 31/03/2015.
 */
public class SmsApi {

    private Context mCtx;
    final SmsManager smsManager = SmsManager.getDefault();

    public SmsApi(Context context){
        mCtx = context;
    }

    public Sms_s getLastSms(Intent intent){
        final Bundle bundle = intent.getExtras();
        Sms_s sms_s = new Sms_s(mCtx);

        try{
            if(bundle!=null){
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for(int i=0; i< pdusObj.length; i++){
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[])pdusObj[i]);

                    sms_s.set_number(currentMessage.getDisplayOriginatingAddress());
                    sms_s.set_message(currentMessage.getDisplayMessageBody());
                    Log.d(getClass().getName().toString(), "senderNum: " + sms_s.get_number() + ":" + sms_s.getContactName() + " - message:\n" + sms_s.get_message());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e(getClass().getName().toString(),"Exception: "+e.getMessage());
        }
        return sms_s;
    }

    public void sendSms(Sms_s sms){
        Intent sentIntent = new Intent("SMS_ACTION_SENT");
        PendingIntent spi = PendingIntent.getBroadcast(mCtx, 0, sentIntent, 0);
        smsManager.sendTextMessage(sms.get_number(),null,sms.get_message(),spi, spi);
    }
}
