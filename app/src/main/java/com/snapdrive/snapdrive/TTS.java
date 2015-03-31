package com.snapdrive.snapdrive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by Mathieu on 31/03/2015.
 */
public class TTS {
    private final int CHECK_CODE = 0x1;
    private final int LONG_DURATION = 5000;
    private final int SHORT_DURATION = 1200;

    private Speaker speaker;
    private BroadcastReceiver smsReceiver;
    private Context context;

    public TTS(Context context){
        this.context = context;
    }
    /*
    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        this.context.startActivityForResult(check, CHECK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHECK_CODE){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                speaker = new Speaker(this.context);
            }else {
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                this.context.startActivity(install);
            }
        }
    }
    */
    private void initializeSMSReceiver(){
        smsReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bundle = intent.getExtras();
                if(bundle!=null){
                    Object[] pdus = (Object[])bundle.get("pdus");
                    for(int i=0;i<pdus.length;i++){
                        byte[] pdu = (byte[])pdus[i];
                        SmsMessage message = SmsMessage.createFromPdu(pdu);
                        String text = message.getDisplayMessageBody();
                        //String sender = getContactName(message.getOriginatingAddress());
                        speaker.pause(LONG_DURATION);
                        speaker.speak("Vous avez un nouveau message de " /*+ sender */+ "!");
                        speaker.pause(SHORT_DURATION);
                        speaker.speak(text);
                        //smsSender.setText("Message from " + sender);
                        //smsText.setText(text);
                    }
                }

            }
        };
    }

    /*private String getContactName(String phone){
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        String projection[] = new String[]{ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }else {
            return "unknown number";
        }
    }

    private void registerSMSReceiver() {
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);
    }*/
}
