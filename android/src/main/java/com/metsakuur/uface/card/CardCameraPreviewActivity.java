package com.metsakuur.uface.card;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.metsakuur.mkga;
import com.metsakuur.uface.BaseActivity;
import com.metsakuur.uface.R;
import com.metsakuur.uface.UFaceConfig;
import com.metsakuur.uface.databinding.ActivityCardCameraPreviewBinding;
import com.metsakuur.ufacedetectormango.util.UFaceLogger;
import com.metsakuur.ufacedetectormango.util.UFaceUtils;
import com.oezsoft.oezfr;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@ExperimentalGetImage
public class CardCameraPreviewActivity extends BaseActivity implements CardListener {
    ActivityCardCameraPreviewBinding binding;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    ExecutorService cameraExecutor = Executors.newSingleThreadExecutor();


    CardAnalyzer cardAnalyzer = null;
    ImageAnalysis imageAnalysis = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCardCameraPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        UFaceConfig.getInstance().licenseKey = getResources().getString(R.string.license);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.viewOverlay.invalidate();

            }
        });
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    initDetector();
                    System.loadLibrary("oezfr");
                    bindPreview(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));


    }

    private void initDetector() {
        String filePath = getFilesDir().getPath();
        for (int i = 0; i < UFaceConfig.getInstance().modelList.length; i++) {
            String strDataFile = filePath + "/" + UFaceConfig.getInstance().modelList[i];
            File file = new File(strDataFile);
            if (!file.exists()) {
                byte[] btFileData = readAsset(this, UFaceConfig.getInstance().modelList[i]);
                saveFile(strDataFile, btFileData);
            } else {
                byte[] btFileData = readAsset(this, UFaceConfig.getInstance().modelList[i]);
                if (btFileData != null && file.length() != btFileData.length) {
                    file.delete();
                    saveFile(strDataFile, btFileData);
                }
            }
        }

        UFaceUtils.INSTANCE.setDebugEnabled(true);

        System.loadLibrary("mkga-mango");
        mkga.MKGAinit(this, UFaceConfig.getInstance().licenseKey, filePath);

    }

    private byte[] readAsset(Context context, String strFileName) {
        AssetManager am = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = am.open(strFileName);
            int size = inputStream.available();
            if (size > 0) {
                byte[] data = new byte[size];
                inputStream.read(data);
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {

                }
            }
        }
        return null;
    }

    private void saveFile(String strFileName, byte[] btData) {
        File file = new File(strFileName);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return;
        }
        try {
            outputStream.write(btData);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();


        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

        imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        cardAnalyzer = new CardAnalyzer(this);
        imageAnalysis.setAnalyzer(cameraExecutor, cardAnalyzer);

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageAnalysis);
    }

    @Override
    public void onCardBitmap(@NonNull Bitmap bitmap) {
        Log.d("onCardBitmap ::: ", "onCardBitmap");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                byte[] byteJpeg = ImageUtils.jpegData(bitmap, 50);
                if (byteJpeg != null) {
                    String base64Image = Base64.encodeToString(byteJpeg, Base64.NO_WRAP);

                    int faceNum = mkga.MKGAdetectJPEG(byteJpeg, byteJpeg.length, 1.0f);
                    UFaceLogger.INSTANCE.d("faceNum = " + faceNum);

                    if (faceNum > 0) {
                        UFaceConfig.getInstance().idCardBase64 = base64Image;
                        setResult(RESULT_OK);
                        finish();

                    } else {
                        if (cardAnalyzer != null) {
                            cardAnalyzer.resetCheck();
                        }
                    }
                }
            }
        });
    }


    @Override
    public void onCardCorner(@NonNull float[] corner, int width, int height) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.viewOverlay.setCorner(corner, width, height);
            }
        });

    }


    @ExperimentalGetImage
    public class CardAnalyzer implements ImageAnalysis.Analyzer {

        private CardListener cardListener;
        private int count = 0;
        private int skipCount = 0;


        public void resetCheck() {
            count = 0;
            skipCount = 0;
        }

        public CardAnalyzer(CardListener cardListener) {
            this.cardListener = cardListener;
        }

        @Override
        public void analyze(ImageProxy image) {
            if (skipCount < 5) {
                skipCount++;
                image.close();
                return;
            }

            if (image.getImage() != null) {
                Image mediaImage = image.getImage();

                // Convert mediaImage to bitmap
                Bitmap bitmap = ImageUtils.imageToBitmap(mediaImage);

                // Rotate the bitmap
                Bitmap rotateBitmap = ImageUtils.rotateBitmap(bitmap, 90f);

                // Convert rotated bitmap to JPEG data
                byte[] byteJpeg = ImageUtils.jpegData(rotateBitmap, 90);


                // Call oezfr().GetEdge2 method
                float[] m_fCorner = new oezfr().GetEdge2(
                        byteJpeg,
                        rotateBitmap.getWidth(),
                        rotateBitmap.getHeight(),
                        100,
                        20
                );

                Log.d("m_fCorner", Arrays.toString(m_fCorner));

                if (m_fCorner != null && m_fCorner.length > 0 && m_fCorner[0] > 0) {
                    count++;

                    if (count < 10) {
                        image.close();
                        return;
                    }

                    // Crop the image
                    android.graphics.Bitmap cropBitmap = ImageUtils.cropImage(
                            rotateBitmap,
                            new Rect(
                                    (int) Math.min(m_fCorner[6], m_fCorner[0]),
                                    (int) Math.min(m_fCorner[7], m_fCorner[5]),
                                    (int) Math.max(m_fCorner[2], m_fCorner[4]),
                                    (int) Math.max(m_fCorner[1], m_fCorner[3])
                            )
                    );

                    if (cropBitmap != null) {
                        cardListener.onCardBitmap(cropBitmap);
                    }
                }
            }
            image.close();
        }
    }


}
