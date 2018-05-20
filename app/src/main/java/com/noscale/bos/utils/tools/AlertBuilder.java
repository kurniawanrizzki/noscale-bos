package com.noscale.bos.utils.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by kurniawanrizzki on 20/05/18.
 */

public abstract class AlertBuilder {
    private Context context;
    public AlertBuilder (Context context) {
        this.context = context;
    }
    public AlertDialog.Builder build (String title, String message, int positiveButtonLabel, int negativeButtonLabel) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title).setMessage(message);
        alertDialog.setPositiveButton(positiveButtonLabel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onPositiveButtonEvent(dialog, which);
            }
        });
        alertDialog.setNegativeButton(negativeButtonLabel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onNegativeButtonEvent(dialog, which);
            }
        });
        return alertDialog;
    }
    protected abstract void onPositiveButtonEvent (DialogInterface dialog, int id);
    protected abstract void onNegativeButtonEvent (DialogInterface dialog, int id);
}
