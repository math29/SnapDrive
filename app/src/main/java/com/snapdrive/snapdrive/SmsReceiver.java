package com.snapdrive.snapdrive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


public class SmsReceiver extends BroadcastReceiver{
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        SmsApi api = new SmsApi(context);
        Sms_s sms = api.getLastSms(intent);
    }
}
