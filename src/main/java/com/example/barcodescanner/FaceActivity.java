package com.example.barcodescanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.barcodescanner.databinding.ActivityFaceBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

import java.io.IOException;
import java.util.List;

public class FaceActivity extends AppCompatActivity {

    ActivityFaceBinding binding;
    private static final String faceTag="faceTag";
    private static final int IMAGE2=121;
    FaceDetectorOptions highAccuracyOpts;
    Uri selectedImage;
    FaceDetector detector;
    InputImage inputImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();
         detector = FaceDetection.getClient(highAccuracyOpts);
        
        binding.face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(FaceActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(IMAGE2);
//                detectFace();
            }
        });
    }//end of onCreate method

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE2 && resultCode == RESULT_OK && data != null){
            if(data.getData()!=null){
                selectedImage=data.getData();
                try {
                    binding.face.setImageURI(selectedImage);
                    detectFace();
                } catch (Exception e) {
                    Log.d("detectFace1()",e.toString());
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void detectFace() throws IOException {
        if (selectedImage == null) {
            Log.d("detectFace2()", "selectedImage is null");
            return;
        }

        try {
              inputImage = InputImage.fromFilePath(FaceActivity.this, selectedImage);

            Task<List<Face>> result = detector.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                        @Override
                        public void onSuccess(List<Face> faces) {
                            Log.d("onSuccess","onSuccess is fired");
                            for (Face face : faces) {
                                Rect bounds = face.getBoundingBox();
                                float rotY = face.getHeadEulerAngleY();
                                float rotZ = face.getHeadEulerAngleZ();

                                FaceLandmark leftEar = face.getLandmark(FaceLandmark.LEFT_EAR);
                                if (leftEar != null) {
                                    PointF leftEarPos = leftEar.getPosition();
                                    Log.d("leftEarPos", String.valueOf(leftEarPos));
                                }

                                List<PointF> leftEyeContour =
                                        face.getContour(FaceContour.LEFT_EYE).getPoints();
                                Log.d("leftEyeContour", String.valueOf(leftEyeContour));
                                List<PointF> upperLipBottomContour =
                                        face.getContour(FaceContour.UPPER_LIP_BOTTOM).getPoints();

                                Float smileProb = face.getSmilingProbability();
                                if (smileProb != null) {
                                    float smileProbability = smileProb;
                                    Log.d("Smile",String.valueOf(smileProbability));
                                }

                                Float rightEyeOpenProb = face.getRightEyeOpenProbability();
                                if (rightEyeOpenProb != null) {
                                    float rightEyeOpenProbability = rightEyeOpenProb;
                                }

                                Integer trackingId = face.getTrackingId();
                                if (trackingId != null) {
                                    int id = trackingId;
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("failed_to_detect", e.toString());
                        }
                    });
        } catch (IOException e) {
            Log.d("detectFace()", e.toString());
            e.printStackTrace();
        }

    }
}

//
//        inputImage = InputImage.fromFilePath(FaceActivity.this, selectedImage);
//        try {
//
//
//            Task<List<Face>> result = detector.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<Face>>() {
//                @Override
//                public void onSuccess(List<Face> faces) {
//                    for (Face face : faces) {
//                        Rect bounds = face.getBoundingBox();
//                        float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
//                        float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees
//
//                        // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
//                        // nose available):
//                        FaceLandmark leftEar = face.getLandmark(FaceLandmark.LEFT_EAR);
//                        if (leftEar != null) {
//                            PointF leftEarPos = leftEar.getPosition();
//                        }
//
//                        // If contour detection was enabled:
//                        List<PointF> leftEyeContour =
//                                face.getContour(FaceContour.LEFT_EYE).getPoints();
//                        List<PointF> upperLipBottomContour =
//                                face.getContour(FaceContour.UPPER_LIP_BOTTOM).getPoints();
//
//
//                        if (face.getSmilingProbability() != null) {
//                            float smileProb = face.getSmilingProbability();
//                        }
//                        if (face.getRightEyeOpenProbability() != null) {
//                            float rightEyeOpenProb = face.getRightEyeOpenProbability();
//                        }
//
//                        // If face tracking was enabled:
//                        if (face.getTrackingId() != null) {
//                            int id = face.getTrackingId();
//                        }
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d("failed_to_detect", e.toString());
//                }
//            });
//        }catch (IOException e){
//            Log.d("detectFace()", e.toString());
//            e.printStackTrace();
//        }