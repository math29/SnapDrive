package com.snapdrive.snapdrive;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecordService extends Service{


    private static final String TAG = "RecordService";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private static Camera mServiceCamera;
    private boolean mRecordingStatus;
    private MediaRecorder mMediaRecorder;


    @Override
    public void onCreate() {
        mRecordingStatus = false;
        mSurfaceView = CameraActivity.mSurfaceView;
        mSurfaceHolder = CameraActivity.mSurfaceHolder;
        // open front camera



        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        // if there are no records running, start one


            if (mRecordingStatus == false)
                startRecording();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopRecording();
        mRecordingStatus = false;
        super.onDestroy();
    }

    /**
     * start a recording
     */
    public boolean startRecording(){
        try {
            Toast.makeText(getBaseContext(), "Recording Started", Toast.LENGTH_SHORT).show();
            mServiceCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            mServiceCamera.reconnect();

            // camera parameters

            Camera.Parameters p = mServiceCamera.getParameters();
            p.setRotation(CameraActivity.rotate);

            final List<Camera.Size> listSize = p.getSupportedPreviewSizes();
            Camera.Size mPreviewSize = listSize.get(2);
            //Log.v(TAG, "use: width = " + mPreviewSize.width
              //      + " height = " + mPreviewSize.height);
            p.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            p.setPreviewFormat(PixelFormat.YCbCr_420_SP);
            mServiceCamera.setParameters(p);
            mServiceCamera.setDisplayOrientation(CameraActivity.rotate);
            // set the preview display, unfortunately we must have it
            try {
                mServiceCamera.setPreviewDisplay(mSurfaceHolder);
                mServiceCamera.startPreview();
            }
            catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
            mServiceCamera.unlock();
            //MediaRecorder
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setCamera(mServiceCamera);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

            // set the filePath in SnapDrive folder
            String path = getOutputMediaFile(2).getPath();
            AppPreferences prefs = new AppPreferences(getApplicationContext());
            prefs.setVideoPath(path);
            mMediaRecorder.setOutputFile(path);
            mMediaRecorder.setVideoFrameRate(30);
            mMediaRecorder.setVideoSize(mPreviewSize.width, mPreviewSize.height);

            mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
            // video duration
            mMediaRecorder.setMaxDuration(5000);
            mMediaRecorder.setOrientationHint(CameraActivity.rotate);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            // listen the end of recording
           mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                        try {
                            stopRecording();
                            Log.v("Ici :", "il se passe quelque chose !!!!!!!!");
                        }catch (IllegalStateException e) {
                            Log.d("Error", "Error accessing file: " + e.getMessage());
                        }
                    }
                }
            });
            mRecordingStatus = true;
            return true;
        } catch (IllegalStateException e) {
            //Log.d(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Stop th recording and launch MMS send(TODO)
     */
    public void stopRecording() {
        Toast.makeText(getBaseContext(), "Recording Stopped", Toast.LENGTH_SHORT).show();
        try {
            mServiceCamera.reconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mServiceCamera.stopPreview();
        mMediaRecorder.release();
        mServiceCamera.release();
        mServiceCamera = null;

        /**
         * Lancement du service pour la réponse
         */
        Intent i = new Intent(getBaseContext(),TTSService.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("action","reponse");
        startService(i);
    }




    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "SnapDrive");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("SnapDrive", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == 2) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }
        Log.d("SnapDrive","name: "+mediaFile.getPath());
        return mediaFile;
    }
}