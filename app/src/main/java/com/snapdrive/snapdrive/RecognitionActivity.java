package com.snapdrive.snapdrive;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by crocus on 05/04/15.
 */
public class RecognitionActivity extends Activity {

    private final int REQ_CODE_TO_SPEECH = 100;
    private final int REQ_CODE_TO_TALK = 99;
    private TextView responseTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recognition);
        if(getIntent().getExtras().getString("action")!=null) {
            String action = getIntent().getExtras().getString("action");
            responseTv = (TextView) findViewById(R.id.textView);
            promptTextToSpeech(action);
        }
    }

    private void promptTextToSpeech(String action){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        if(action.equals("talk")) {
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getResources().getString(R.string.talk));
        }else{
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,getResources().getString(R.string.reponse));
        }
        try{
            if(action.equals("talk")) {
                startActivityForResult(intent, REQ_CODE_TO_TALK);
            }else{
                startActivityForResult(intent, REQ_CODE_TO_SPEECH);
            }
        }catch (ActivityNotFoundException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Activity not found",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE_TO_SPEECH:
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    responseTv.setText(result.get(0));
                    if(result.get(0).equals(getResources().getString(R.string.yes))) {
                        Intent i = new Intent(getApplicationContext(), TTSService.class);
                        //Toast.makeText(context, "message: " + sms.get_message(), Toast.LENGTH_SHORT).show();
                        i.putExtra("action", "talk");
                        startService(i);
                        finish();
                    }
                }
                break;
            case REQ_CODE_TO_TALK:
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    responseTv.setText(result.get(0));
                    AppPreferences prefs = new AppPreferences(getApplicationContext());
                    Sms_s sms = new Sms_s(getApplicationContext(),prefs.getNumber(),result.get(0));
                    SmsApi api = new SmsApi(getApplicationContext());
                    api.sendSms(sms);
                    finish();
                }
        }
    }
}
