package com.snapdrive.snapdrive;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Yoyo on 31/03/2015.
 */
public class TTSService extends Service implements TextToSpeech.OnInitListener{

    private String sender, message;
    private TextToSpeech mTts;
    private static final String TAG="TTSService";

    @Override

    public IBinder onBind(Intent arg0) {
        Log.d(TAG,"Key: "+arg0.getExtras().getString("Message"));
        return null;
    }


    @Override
    public void onCreate() {

        mTts = new TextToSpeech(this,
                this  // OnInitListener
        );

        mTts.setSpeechRate(0.8f);
        Log.v(TAG, "oncreate_service");
        sender ="UNKNOWN";
        message = "No message";
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {

        sender = intent.getExtras().getString("Sender");
        message = intent.getExtras().getString("Message");
        say("Message reçu de "+sender);
        mTts.playSilence(1500, TextToSpeech.QUEUE_ADD,null);
        say(message);

        Log.v(TAG, "onstart_service");
        super.onStart(intent, startId);
    }

    @Override
    public void onInit(int status) {
        Log.v(TAG, "oninit");
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.getDefault());
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.v(TAG, "Language is not available.");
            } else {
                say("Message reçu de "+sender);
                mTts.playSilence(1500, TextToSpeech.QUEUE_ADD,null);
                say(message);

            }
        } else {
            Log.v(TAG, "Could not initialize TextToSpeech.");
        }
    }
    private void say(String str) {
        mTts.speak(str,
                TextToSpeech.QUEUE_ADD,
                null);
    }
}