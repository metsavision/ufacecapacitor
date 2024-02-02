package com.metsakuur.uface.camera;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.metsakuur.uface.BaseActivity;
import com.metsakuur.uface.R;
import com.metsakuur.uface.UFaceConfig;
import com.metsakuur.uface.card.ImageUtils;
import com.metsakuur.uface.databinding.ActivityFaceCameraBinding;
import com.metsakuur.uface.retrofit.UFaceAPIResult;
import com.metsakuur.uface.retrofit.UFaceRequestData;
import com.metsakuur.uface.retrofit.UFaceRetrofitManager;
import com.metsakuur.ufacedetectormango.UFaceDetector;
import com.metsakuur.ufacedetectormango.UFaceDetectorListener;
import com.metsakuur.ufacedetectormango.model.UFaceError;
import com.metsakuur.ufacedetectormango.model.UFaceResult;
import com.metsakuur.ufacedetectormango.util.UFaceLogger;
import com.metsakuur.ufacedetectormango.util.UFaceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaceCameraActivity extends BaseActivity implements UFaceDetectorListener {
    private ActivityFaceCameraBinding binding;


    private UFaceDetector uFaceDetector = null;

    private Float spoofThreshold = 0.6f;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFaceCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        UFaceConfig.getInstance().licenseKey = getResources().getString(R.string.license);

        initDetector();
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

        uFaceDetector = new UFaceDetector();
        uFaceDetector.setAntiSpoofingUse(
                true, true, false, false,
                false, true, spoofThreshold, false);
        binding.previewView.setClipToOutline(true);
        uFaceDetector.setPreviewView(binding.previewView);
        uFaceDetector.setFaceDetectorListener(this);
        uFaceDetector.initDetector(this, UFaceConfig.getInstance().licenseKey, filePath);
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

    @Override
    public void uFaceDetectLargeFace() {
        binding.cameraText.setText(getString(R.string.face_result_too_big));
    }

    @Override
    public void uFaceDetectPitchYawRoll(float v, float v1, float v2) {

    }

    @Override
    public void uFaceDetectSmallFace() {
        binding.cameraText.setText(getString(R.string.face_result_too_small));

    }

    @Override
    public void uFaceDetector(@NonNull UFaceDetector detector, @NonNull UFaceError error) {
        UFaceLogger.INSTANCE.d("" + error.getErrorDescription());
        if (error.getErrorCode().equals("72001")) {
            detector.reStartDetector();
        } else {
            openAlertView(error.getErrorDescription() + "(code: " + error.getErrorCode() + ")", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
    }

    @Override
    public void uFaceDetector(@NonNull UFaceDetector uFaceDetector, @NonNull UFaceResult result) {
        UFaceLogger.INSTANCE.d("result ::: " + result.toString());

        if (result.isMask()) {
            binding.cameraText.setText(getString(R.string.face_result_mask));
            uFaceDetector.reStartDetector();
        } else if (result.isFake()) {
            binding.cameraText.setText(getString(R.string.face_show_face_msg));
            uFaceDetector.reStartDetector();
        } else {
            requestIdVerify(result);

        }

    }

    @Override
    public void uFaceIsDetectFace(boolean isDetectFace) {
        if (!isDetectFace) {
            binding.cameraText.setText(getString(R.string.face_show_face_msg));
        }
    }

    @Override
    public void uFaceSetCameraComplete() {
        uFaceDetector.startDetect();
    }

    public void requestIdVerify(UFaceResult result) {
        byte[] dataImg = ImageUtils.jpegData(result.getFullImage(), 90);
        String database64 = Base64.encodeToString(dataImg, Base64.DEFAULT);

        if (database64 != null) {

            UFaceRequestData uFaceRequestData = new UFaceRequestData();
            uFaceRequestData.setId(UFaceConfig.getInstance().idCardBase64);
            uFaceRequestData.setFace(database64);
            UFaceRetrofitManager.getInstance().requestApiRetrofit("verifyid", uFaceRequestData, new Callback<UFaceAPIResult>() {
                @Override
                public void onResponse(Call<UFaceAPIResult> call, Response<UFaceAPIResult> response) {
                    if (response.isSuccessful()) {
                        UFaceConfig.getInstance().scoreValue = response.body().getData().toString();
                        UFaceLogger.INSTANCE.d("resultValue ::: " + UFaceConfig.getInstance().scoreValue);

                        Intent intent = new Intent();
                        intent.putExtra("score", UFaceConfig.getInstance().scoreValue);
                        UFaceLogger.INSTANCE.d("score ::: " + UFaceConfig.getInstance().scoreValue);
                        setResult(RESULT_OK, intent);
                        finishAndRemoveTask();
                    } else {
                        openAlertView("error ::: " + response.message() + " (code: " + response.code(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<UFaceAPIResult> call, Throwable t) {
                    openAlertView("server onFailure ::: " + t.getMessage(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    t.printStackTrace();
                }
            });

        }
    }

}


