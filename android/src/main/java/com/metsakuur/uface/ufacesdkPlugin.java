package com.metsakuur.uface;

import android.content.Intent;

import androidx.activity.result.ActivityResult;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.metsakuur.uface.camera.FaceCameraActivity;
import com.metsakuur.uface.card.CardCameraPreviewActivity;

@CapacitorPlugin(name = "ufacesdk")
public class ufacesdkPlugin extends Plugin {

    private ufacesdk implementation = new ufacesdk();

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void verify(PluginCall call) {
        String token = call.getString("token");
        String baseurl = call.getString("baseurl");

        UFaceConfig.getInstance().BASE_URL = baseurl;
        UFaceConfig.getInstance().TOKEN = token;
        startActivityForIdCard(call);
    }

    public void startActivityForIdCard(PluginCall call) {
        Intent intent = new Intent(getContext(), CardCameraPreviewActivity.class);
        startActivityForResult(call, intent, "processNextRequest");
    }

    @ActivityCallback
    public void processNextRequest(PluginCall call, ActivityResult result) {
        startActivityForFace(call);
    }

    public void startActivityForFace(PluginCall call) {
        Intent intent = new Intent(getContext(), FaceCameraActivity.class);
        startActivityForResult(call, intent, "processResult");
    }

    @ActivityCallback
    public void processResult(PluginCall call, ActivityResult result) {
        Intent intent = result.getData();
        String score = "";
        if (intent != null) {
            score = intent.getStringExtra("score");
        }
        JSObject ret = new JSObject();
        ret.put("value", score);
        notifyListeners("onVerified", ret);
    }
}
