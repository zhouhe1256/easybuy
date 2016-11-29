package com.gaiya.easybuy.util;

import android.view.Gravity;
import android.widget.Toast;

import com.gaiya.easybuy.application.GApplication;

/**
 * Created by dengt on 15-9-30.
 */
public class ToastUtil {
    public static void showMessage(String message) {
        Toast toast = Toast.makeText(GApplication.getInstance(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }
}
