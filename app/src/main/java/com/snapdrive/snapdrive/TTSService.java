package com.snapdrive.snapdrive;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.speech.RecognitionService;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Yoyo on 31/03/2015.
 */
public class TTSService extends Service implements TextToSpeech.OnInitListener{

    private String action, sender, message;
    private TextToSpeech mTts;
    private static final String TAG="TTSService";
    private boolean init = false;
    private boolean next = false;

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

        mTts.setSpeechRate(0.9f);
        Log.v(TAG, "oncreate_service");
        action = "UNKNOWN";
        sender ="UNKNOWN";
        message = "No message";
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if(intent.getExtras().getString("action")!=null) {
            action = intent.getExtras().getString("action");
            sender = intent.getExtras().getString("Sender");
            message = intent.getExtras().getString("Message");
            //say(getResources().getString(R.string.sms_received)+sender);
            //mTts.playSilence(1500, TextToSpeech.QUEUE_ADD,null);
            //say(message);
            if (init) {
                if (action.equals("message")) {
                    say(getResources().getString(R.string.sms_received) + sender, "sender");
                    mTts.playSilence(1500, TextToSpeech.QUEUE_ADD, null);
                    say(message, "message");
                } else if (action.equals("activation")) {
                    say(getResources().getString(R.string.activation), "activation");
                } else if (action.equals("desactivation")) {
                    say(getResources().getString(R.string.desactivation), "desactivation");
                }else if(action.equals("reponse")){
                    say(getResources().getString(R.string.reponse),"reponse");
                }else if(action.equals("talk")){
                    say(getResources().getString(R.string.talk),"talk");
                }else if(action.equals("choice")){
                    mTts.playSilence(1500, TextToSpeech.QUEUE_ADD, null);
                    say(getResources().getString(R.string.response_type),"choice");
                }
            }
        }
        Log.v(TAG, "onstart_service");
        super.onStart(intent, startId);
    }

    @Override
    public void onInit(int status) {
        Log.v(TAG, "oninit");
        Log.d(TAG, "Action: "+action);
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.getDefault());
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.v(TAG, "Language is not available.");
            } else {
                init = true;
                if(action.equals("message")) {
                    say(getResources().getString(R.string.sms_received) + sender,"sender");
                    mTts.playSilence(1500, TextToSpeech.QUEUE_ADD, null);
                    say(message,"message");
                }else if(action.equals("activation")){
                    say(getResources().getString(R.string.activation),"activation");
                }else if(action.equals("desactivation")){
                    say(getResources().getString(R.string.desactivation),"desactivation");
                }else if(action.equals("reponse")){
                    say(getResources().getString(R.string.reponse),"reponse");
                }else if(action.equals("talk")){
                    say(getResources().getString(R.string.talk),"talk");
                }else if(action.equals("choice")){
                    mTts.playSilence(1500, TextToSpeech.QUEUE_ADD, null);
                    say(getResources().getString(R.string.response_type),"choice");
                }
                mTts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                    @Override
                    public void onUtteranceCompleted(String utteranceId) {
                        if(utteranceId.equals("message")) {
                            action = "choice";
                            say(getResources().getString(R.string.response_type),"choice");
                        }else if(utteranceId.equals("reponse")){
                            Intent i = new Intent(getApplicationContext(), RecognitionActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("action","reponse");
                            startActivity(i);
                        }else if(utteranceId.equals("talk")){
                            Log.d(TAG,"TALK");
                            Intent i = new Intent(getApplicationContext(), RecognitionActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("action","talk");
                            startActivity(i);
                        }else if(utteranceId.equals("choice")){
                            Log.d(TAG,"choice");
                            Intent i = new Intent(getApplicationContext(), RecognitionActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("action","choice");
                            startActivity(i);
                        }
                    }
                });

            }
        } else {
            Log.v(TAG, "Could not initialize TextToSpeech.");
        }
    }



    private void say(String str, String action) {
        HashMap<String, String> myHashAlarm = new HashMap<String, String>();
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, action);
        mTts.speak(str,
                TextToSpeech.QUEUE_ADD,
                myHashAlarm);
    }
}