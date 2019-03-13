package io.hussam.westarmy.onpullman;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.security.Permission;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.hussam.westarmy.onpullman.pullmans.PullmansActivity;

import io.hussam.westarmy.onpullman.R;
import io.hussam.westarmy.onpullman.util.PermissionUtil;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity extends AppCompatActivity {

    private boolean permissionResolved = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_act);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermissionAndProceed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissionAndProceed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        checkPermissionAndProceed();
    }

    private void checkPermissionAndProceed() {
        if (PermissionUtil.doesAppNeedPermission()) {
            if (!PermissionUtil.isPermissionGranted(this, WRITE_EXTERNAL_STORAGE)) {
                PermissionUtil.askForPermission(this, "برجاء اعطاء صلاحيه تخزين الصور", 101, WRITE_EXTERNAL_STORAGE);
                permissionResolved = false;
            }
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (permissionResolved) {
                    startActivity(new Intent(SplashActivity.this, PullmansActivity.class));
                    finish();
                }
            }
        }, 3000);
    }
}
