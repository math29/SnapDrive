package com.snapdrive.snapdrive;

import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    //private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    Cam cam;
    Camera mCamera;
    CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCamera = cam.getCameraInstance();

        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        //mCamera.takePicture(null, null, mPicture);

        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, cam.mPicture);
                    }
                }
        );

        new TakePhoto().execute("");
        //takePhoto(captureButton);
    }

    private void takePhoto(Button captureButton){
        captureButton.performClick();
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


    private class TakePhoto extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            TextView txt = (TextView) findViewById(R.id.textView);
            txt.setText("Executed"); // txt.setText(result);
            mCamera.takePicture(null, null, cam.mPicture);
            //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            //Toast.makeText(this, "Image saved to:\n" +
              //      data.getData(), Toast.LENGTH_LONG).show();
            //savefile(fileUri);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }
    }





}
