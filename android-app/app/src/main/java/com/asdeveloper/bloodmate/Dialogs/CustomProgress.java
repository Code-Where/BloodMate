package com.asdeveloper.bloodmate.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.asdeveloper.bloodmate.R;

public class CustomProgress {
    Dialog dialog;

    public CustomProgress(Context context){
        dialog = new Dialog(context);
    }
    public CustomProgress(Context context, String title, String message, boolean isCancellable){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.progress_dialog);
        TextView progressTitle = dialog.findViewById(R.id.progressTitle);
        TextView progressMessage = dialog.findViewById(R.id.progressMessage);
        progressTitle.setText(title);
        progressMessage.setText(message);
        dialog.setCancelable(isCancellable);
    }

    public void setProgress(String title, String message, boolean isCancellable){
        dialog.setContentView(R.layout.progress_dialog);
        TextView progressTitle = dialog.findViewById(R.id.progressTitle);
        TextView progressMessage = dialog.findViewById(R.id.progressMessage);
        progressTitle.setText(title);
        progressMessage.setText(message);
        dialog.setCancelable(isCancellable);
    }

    public void showDialog(){
        dialog.show();
    }
    public void dismissDialog(){
        if (dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
