package com.example.g6podbookingsystem.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.g6podbookingsystem.R;

public class LoadingDialog {
    private Dialog dialog;
    private Context context;

    public LoadingDialog(Context context) {
        this.context = context;
    }

    public void show(String message) {
        if (dialog != null && dialog.isShowing()) return;

        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        dialog.setContentView(view);

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Áp dụng animation xoay
        ImageView imgChair = view.findViewById(R.id.imgChair);
        Animation rotate = AnimationUtils.loadAnimation(context, R.anim.rotate_spin);
        imgChair.startAnimation(rotate);

        dialog.show();
    }

    public void show() {
        show("Đang tải...");
    }

    public void hide() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
