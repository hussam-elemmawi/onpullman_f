package io.hussam.westarmy.onpullman.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class PermissionUtil {

    public static boolean doesAppNeedPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isPermissionGranted(Context context, String... permissions) {
        boolean result = true;
        for (String permission : permissions) {
            result &= ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return result;
    }

    public static void askForPermission(final Activity context, String whyStringRes, final int permissionCode, final String... permissions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(whyStringRes);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "حسنا",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ActivityCompat.requestPermissions(context, permissions, permissionCode);
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
