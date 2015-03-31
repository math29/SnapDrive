package com.snapdrive.snapdrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ToggleButton toggle = (ToggleButton)findViewById(R.id.speechToggle);
        //smsText = (TextView)findViewById(R.id.sms_text);
        //smsSender = (TextView)findViewById(R.id.sms_sender);

        /*CompoundButton.OnCheckedChangeListener toggleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
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
                }
            }
        };
        toggle.setOnCheckedChangeListener(toggleListener);*/

        /*checkTTS();
        initializeSMSReceiver();
        registerSMSReceiver();*/
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


}
