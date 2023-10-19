package com.example.practiseprojec1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.practiseprojec1.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private static final int  PERMISSIONS_CODE = 2;

    ActivityMainBinding binding;
    MediaRecorder recorder=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Animation animation= AnimationUtils.loadAnimation(this, R.anim.wellcome);
            binding.welcome.setAnimation(animation);

          binding.welcome.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Toast.makeText(MainActivity.this, "Welcome is clicked", Toast.LENGTH_SHORT).show();
                  recorder = new MediaRecorder();
                      startRecord();
                  Toast.makeText(MainActivity.this, "Record started", Toast.LENGTH_SHORT).show();

              }
          });
          binding.alright.setOnClickListener(v -> {
              stopRecord();
              Toast.makeText(MainActivity.this, "Recording stopped", Toast.LENGTH_SHORT).show();

          });

    }
    //Todo end of onCreate method
    // Requesting permission to RECORD_AUDIO



    // throws IllegalStateException, IOException
    private void doStartRecord(){

        File outputFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), System.currentTimeMillis() + ".amr");
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
      //  recorder.setOutputFile("/sdcard/Music/"+System.currentTimeMillis()+".amr");
        recorder.setOutputFile(outputFile.getPath());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("LOG_TAG", e.getMessage());
        }

        recorder.start();
    }

    private void stopRecord(){
        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;
    }




    private void startRecord() {
        // Check if the permission to record audio is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Request the audio recording permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }

        // Check if the permission to use the camera is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request the camera permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            // All required permissions are granted, proceed with your functionality
           try{
              // doStartRecord();
           }catch (Exception e){
               Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
           }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_RECORD_AUDIO_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, now you can start recording
                // doStartRecord();
                Toast.makeText(this, "Record audio Request granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission to record audio denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode==REQUEST_CAMERA_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, now you can start recording
                // doStartRecord();
                Toast.makeText(this, "camera Request granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission to camera denied", Toast.LENGTH_SHORT).show();
            }
        }

    }



}



