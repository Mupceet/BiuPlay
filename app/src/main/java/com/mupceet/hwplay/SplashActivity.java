package com.mupceet.hwplay;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mupceet.hwplay.home.HomeActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int PERMISSION_REQUEST_CODE_STORAGE = 1;
    private final String[] PERMISSION_STORAGE = {
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
        goHomeActivityWithCheckPermission();
    }

    private void goHomeActivityWithCheckPermission() {
        if (EasyPermissions.hasPermissions(this, PERMISSION_STORAGE)) {
            goHomeActivity();
        } else {
            EasyPermissions.requestPermissions(this, null,
                    PERMISSION_REQUEST_CODE_STORAGE, PERMISSION_STORAGE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> list) {
        // 申请权限成功
        Toast.makeText(this, "授权SD卡权限成功", Toast.LENGTH_SHORT).show();
        goHomeActivity();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> list) {
        // 申请权限失败
        Toast.makeText(this, "授权SD卡权限失败，可能会影响应用的使用",
                Toast.LENGTH_SHORT).show();
        goHomeActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
