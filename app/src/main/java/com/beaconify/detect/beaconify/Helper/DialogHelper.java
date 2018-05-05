package com.beaconify.detect.beaconify.Helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogHelper {

    private Context context;
    private ProgressDialog progressDialog;

    public DialogHelper(Context context)
    {
        this.context = context;
    }

    public void showProgressDialog(String message)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(message);
        progressDialog.show();
    }

    public void hideProgressDialog()
    {
        if(progressDialog == null && !progressDialog.isShowing())
            return;

        progressDialog.hide();
        progressDialog = null;
    }

    public void createAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this.context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        alertDialog.show();
    }

}
