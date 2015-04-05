package com.snapdrive.snapdrive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;



public class MainActivity extends ActionBarActivity {

    AppPreferences prefs;
    ToggleButton toggle;
    Context context;
    Button historicButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggle = (ToggleButton)findViewById(R.id.speechToggle);
        toggle.setText(null);
        toggle.setTextOn(null);
        toggle.setTextOff(null);
        historicButton = (Button) findViewById(R.id.historic_button);
        //smsText = (TextView)findViewById(R.id.sms_text);
        //smsSender = (TextView)findViewById(R.id.sms_sender);
        prefs = new AppPreferences(this);
        context = getApplicationContext();

        CompoundButton.OnCheckedChangeListener toggleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                prefs.activate(isChecked);
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.animbutton);
                toggle.startAnimation(hyperspaceJumpAnimation);
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
        CompoundButton.OnClickListener historicListenner = new CompoundButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Historic.class);
                startActivity(intent);
            }
        };
        toggle.setOnCheckedChangeListener(toggleListener);
        historicButton.setOnClickListener(historicListenner);
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
