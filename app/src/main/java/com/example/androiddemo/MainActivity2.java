package com.example.androiddemo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MainActivity2 extends AppCompatActivity
{
    private EditText mData;
    private ActivityResultLauncher<String> mCameraPermissionRequest;
    private Boolean mbCameraPermission;
    private PreviewView mPreviewView;
    private ImageCapture mImageCapture;
    private Camera mCamera;
    private ScheduledExecutorService mCameraBackgroundExecutor;

    private ListenableFuture<ProcessCameraProvider> mCameraProviderFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mData = findViewById(R.id.editTextData);
        mPreviewView = findViewById(R.id.viewFinder);

        mCameraPermissionRequest =
            registerForActivityResult (new ActivityResultContracts.RequestPermission(),
                result -> {
                    mbCameraPermission = result;
                }
            );
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_GRANTED)
        {
            mCameraPermissionRequest.launch(Manifest.permission.CAMERA);
        }

        mCameraProviderFuture = ProcessCameraProvider.getInstance(this);
        mCameraProviderFuture.addListener(() ->
        {
            try
            {
                ProcessCameraProvider cameraProvider = mCameraProviderFuture.get();
                bindPreview(cameraProvider); // setup camera in the future
            } catch (ExecutionException | InterruptedException e)
            {
                // No errors need to be handled for this Future.
                // This should never be reached.

            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview (ProcessCameraProvider cameraProvider)
    {
        mImageCapture = new ImageCapture.Builder().setTargetRotation(
            this.getDisplay().getRotation()).build();

        CameraSelector cameraSelector = new CameraSelector.Builder().
            requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        Preview preview = new Preview.Builder().build();

        preview.setSurfaceProvider(mPreviewView.getSurfaceProvider());

        try
        {
            mCamera = cameraProvider.bindToLifecycle((LifecycleOwner) this,
                cameraSelector, preview, mImageCapture);
        }
        catch (Exception e)
        {
            Log.e("camera", e.toString());
        }
    }

    public void onClickGo(View view)
    {
        DataStore myData = new DataStore("hi", "CS260");
        Log.d("EVENT", "GO BUTTON PRESSED");

        Intent intent = new Intent(this, MainActivity.class);
        String message = mData.getText().toString();

        intent.putExtra(Intent.EXTRA_TEXT, message);

        //startActivity(intent);

        getPicture(view, myData, intent);
    }

    private void getPicture (View view, DataStore myData, Intent intent)
    {
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        ImageCapture.OutputFileOptions outputFileOptions =
            new ImageCapture.OutputFileOptions.Builder(byteArrayStream).build();
        mCameraBackgroundExecutor =
            Executors.newSingleThreadScheduledExecutor();

        mImageCapture.takePicture(outputFileOptions,
            mCameraBackgroundExecutor,
            new ImageCapture.OnImageSavedCallback() {
                @Override
                public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                    // the image is in byteArrayStream!
                    // add image to DataStore
                    myData.setImage(byteArrayStream.toByteArray());
                    intent.putExtra ("DATA", myData);
                    startActivity(intent);
                }
                @Override
                public void onError(ImageCaptureException error) {
                    Log.d("Camera: ", "error!" + error.toString());
                    startActivity(intent);
                }
            });
    }

    protected void onPause()
    {
        super.onPause();
        if(null != mCameraBackgroundExecutor)
        {
            mCameraBackgroundExecutor.shutdown();
            mCameraBackgroundExecutor = null;
        }
    }
}