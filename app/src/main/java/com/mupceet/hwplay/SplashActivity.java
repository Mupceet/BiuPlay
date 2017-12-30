package com.mupceet.hwplay;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE_STORAGE = 1;
    private String[] PERMISSION_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        sp = getSharedPreferences(Constant.PACKAGE_NAME, MODE_PRIVATE);
        boolean isFirst = sp.getBoolean(Constant.SP_IS_FIRST, true);
        if (!isFirst) {
            goHomeActivity();
        }
    }

    private void goHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @OnClick(R.id.button_enter)
    public void goHome() {
        sp.edit().putBoolean(Constant.SP_IS_FIRST, false).apply();
        verifyStoragePermission(this);
    }

    private void verifyStoragePermission(Activity activity) {
        int checkPermission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (checkPermission == PermissionChecker.PERMISSION_GRANTED) {
            goHomeActivity();
        } else {
            ActivityCompat.requestPermissions(activity,
                    PERMISSION_STORAGE, PERMISSION_REQUEST_CODE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 因为我们只申请了一组权限，就不需要使用 requestCode 进行区分了
        if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
            // 申请权限成功
            Toast.makeText(this, "授权SD卡权限成功", Toast.LENGTH_SHORT).show();
        } else {
            // 申请权限失败
            Toast.makeText(this, "授权SD卡权限失败，可能会影响应用的使用", Toast.LENGTH_SHORT).show();
        }
        goHomeActivity();
    }
}
