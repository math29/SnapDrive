package com.snapdrive.snapdrive;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    //private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    Cam cam;
    Camera mCamera;
    CameraPreview mPreview;
    private MediaRecorder mMediaRecorder;
    private Handler handler;
    private Surface surface;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Pour afficher notre Camera dans une view */
        //mPreview = new CameraPreview(this, mCamera);
        //FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        //preview.addView(mPreview);

        //mCamera.startPreview();

        /* To Take Picture */
        //new TakePhoto().execute("");

        /* To Take Movie */
        if(! prepareVideoRecorder()){
            Log.v("Error", "Problemmm !");
        }

        /*handler = new Handler();
        Runnable r=new Runnable()
        {
            public void run()
            {

                mMediaRecorder.stop();Log.v("Blah :", "Ici il se passe quelque chose");
                releaseMediaRecorder();
                mCamera.lock();
                mCamera.release();

            }

        };
        handler.postDelayed(r, 4000);*/

        mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    try {
                        mMediaRecorder.stop();
                        Log.v("Ici :", "il se passe quelque chose !!!!!!!!");
                    }catch (IllegalStateException e) {
                        Log.d("Error", "Error accessing file: " + e.getMessage());
                    }

                    releaseMediaRecorder();

                }
            }
        });

        CharSequence text = "Movie sauvegardee !";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            TextView txt = (TextView) findViewById(R.id.textView);
            txt.setText("Executed"); // txt.setText(result);
            mCamera.takePicture(null, null, mPicture);
            CharSequence text = "Image sauvegardee !";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        }
    }


    public Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = cam.getOutputMediaFile(MEDIA_TYPE_IMAGE);

            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;

            if (pictureFile == null){
                Log.d("Error", "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("Error", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("Error", "Error accessing file: " + e.getMessage());
            }
        }
    };

    private boolean prepareVideoRecorder(){

        mCamera = cam.getCameraInstance(this);

        /*Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break; //Natural orientation
            case Surface.ROTATION_90:
                degrees = 90;
                break; //Landscape left
            case Surface.ROTATION_180:
                degrees = 180;
                break;//Upside down
            case Surface.ROTATION_270:
                degrees = 270;
                break;//Landscape right
        }
        int rotate = (info.orientation - degrees + 180) % 360;

        //STEP #2: Set the 'rotation' parameter
        Camera.Parameters params = mCamera.getParameters();
        params.setRotation(rotate);
        mCamera.setParameters(params);*/

        mCamera.unlock();

        mMediaRecorder = new MediaRecorder();

        //mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        //mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

        // Step 1: Unlock and set camera to MediaRecorder
        //surface = new Surface(new SurfaceTexture(MEDIA_TYPE_IMAGE));
        //mMediaRecorder.setPreviewDisplay(surface);
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(CamcorderProfile.get(1 , CamcorderProfile.QUALITY_HIGH));
        //mMediaRecorder.setVideoFrameRate(15);

        //mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        //mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

        // Step 4: Set output file
        mMediaRecorder.setOutputFile(cam.getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
        //mMediaRecorder.setOutputFile("/sdcard/myvideo.mp4");

        // Step 5: Set the preview output
        //mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());


        mMediaRecorder.setMaxDuration(6000);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d("Error", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d("Error", "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }

        mMediaRecorder.start();


    return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }


    private class StopMovie extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }

            Log.v("etape fin", "ici c'est la fin !!!!!!");
            mMediaRecorder.stop();
            releaseMediaRecorder();
            mCamera.lock();
            mCamera.release();

            return "Executed";

        }

        @Override
        protected void onPostExecute(String result) {
            //TextView txt = (TextView) findViewById(R.id.textView);
            //txt.setText("Executed"); // txt.setText(result);

            //mCamera.takePicture(null, null, mPicture);
            CharSequence text = "Movie sauvegardee !";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        }
    }
}
