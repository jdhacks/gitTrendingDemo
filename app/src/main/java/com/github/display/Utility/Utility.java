package com.github.display.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.github.display.Interfaces.AlertDialogCallback;
import com.github.display.R;

import java.util.Objects;

public class Utility {
    public static ProgressDialog dialog = null;

    public static boolean checkProgressOpen() {
        return dialog != null && dialog.isShowing();
    }

    public static void showProgress(Context context) {
        if (checkProgressOpen())
            return;

        dialog = ProgressDialog.show(context, null, null);
        ProgressBar spinner = new ProgressBar(context, null, android.R.attr.progressBarStyle);
        spinner.getIndeterminateDrawable().setColorFilter(Color.parseColor("#2D2D2D"), android.graphics.PorterDuff.Mode.SRC_IN);
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(spinner);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void alertDialog(Activity context, String message, boolean cancelable) {

        if (context != null) {
            Dialog dialog = new Dialog(context);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(cancelable);
            /*dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);*/
            /*dialog.getWindow().setGravity(Gravity.TOP);*/
            dialog.setContentView(R.layout.dialog_warning);

            AppCompatTextView txtTitle, yes_button;

            yes_button = dialog.findViewById(R.id.btnokWarning);
            txtTitle = dialog.findViewById(R.id.txt_warning);
            if (isNetworkAvailable(context)) {
                txtTitle.setText(message);
                // txtTitle.setText(context.getString(R.string.general_error));
                try {
                    if (!dialog.isShowing()) {
                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                    }
                } catch (Exception e) {
                    Log.e("TAG","PARSE ERROR"+e.getMessage());
                }
            } else {
                Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                Utility.nointernetAlertDialog(context, context.getString(R.string.no_internet), false, () -> {

                });
            }

            yes_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.onBackPressed();
                    dialog.dismiss();
                }
            });

        }
    }

    public static void nointernetAlertDialog(Context context, String message, boolean cancelable, AlertDialogCallback alertDialogCallback) {

        if (context != null) {
            Dialog dialog = new Dialog(context);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(cancelable);
            /*dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);*/
            /*dialog.getWindow().setGravity(Gravity.TOP);*/
            dialog.setContentView(R.layout.dialo_no_internet);

            AppCompatTextView txt_warning;
            AppCompatTextView txt_header, btnokWarning;

            btnokWarning = dialog.findViewById(R.id.btnokWarning);
            txt_header = dialog.findViewById(R.id.txt_header);
            txt_warning = dialog.findViewById(R.id.txt_warning);
            txt_warning.setText(message);

            txt_header.setText(context.getString(R.string.no_internet_header));
            btnokWarning.setOnClickListener(v -> {
                dialog.dismiss();
                alertDialogCallback.okClick();
            });
            try {
                if (!dialog.isShowing()) {
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    dialog.show();
                }
                ;
            } catch (Exception e) {
                Log.e("TAG","PARSE ERROR"+e.getMessage());
            }
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } else {
            return false;
        }
    }

    public static void cancelProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
            dialog = null;
        }
    }

    public static CircularProgressDrawable getCircularProgressDrawable(@NonNull Context context) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStyle(CircularProgressDrawable.DEFAULT);
        circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(context, R.color.black));
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }
}
