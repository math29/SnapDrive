package com.snapdrive.snapdrive;

import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


import android.media.MediaRecorder;
import android.net.Uri;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import android.view.Surface;



public class MainActivity extends ActionBarActivity {



    AppPreferences prefs;
    ToggleButton toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toggle = (ToggleButton)findViewById(R.id.speechToggle);
        //smsText = (TextView)findViewById(R.id.sms_text);
        //smsSender = (TextView)findViewById(R.id.sms_sender);
        prefs = new AppPreferences(this);


        CompoundButton.OnCheckedChangeListener toggleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                prefs.activate(isChecked);
                /*
                if(isChecked){
                    //speaker.allow(true);
                    //speaker.speak(getString(R.string.start_speaking));
                    //startActivity(new Intent(getApplicationContext(),SpeakActivity.class));
                    Intent i = new Intent(getApplicationContext(),TTSService.class);
                    i.putExtra("Message","Tabernak petit branquignol");
                    i.putExtra("Sender","Yoann Diquélou");
                    startService(i);
                }else{
                    //speaker.speak(getString(R.string.stop_speaking));
                    //speaker.allow(false);
                }*/
            }
        };
        toggle.setOnCheckedChangeListener(toggleListener);

        /*Intent i = new Intent(this, RecognitionActivity.class);
        i.putExtra("action","reponse");
        startActivity(i);*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refresh(){
        toggle.setChecked(prefs.isActivate());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();

    }
}
