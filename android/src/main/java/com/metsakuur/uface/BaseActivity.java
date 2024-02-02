package com.metsakuur.uface;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    Context context;

    public void openAlertView(String msg, DialogInterface.OnClickListener onClickListener) {
        context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (onClickListener == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setMessage(msg).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setMessage(msg).setPositiveButton("OK", onClickListener);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }

}
