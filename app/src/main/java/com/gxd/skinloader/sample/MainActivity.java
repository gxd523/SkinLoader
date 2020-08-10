package com.gxd.skinloader.sample;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gxd.skinloader.SkinListener;
import com.gxd.skinloader.SkinManager;
import com.gxd.skinloader.attr.AttrFactory;

import java.io.File;

public class MainActivity extends BaseActivity {
    private Button button;
    private ViewGroup rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.activity_btn);
        button.post(() -> button.setText(SkinManager.INSTANCE.isDefaultSkin() ? "白色主题" : "黑色主题"));
        rootView = findViewById(R.id.activity_main_root);

        handlePermissions(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void handlePermissions(String... permissions) {
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{permission}, 0);// 如果用户选择禁止后不在询问，则下次启动requestPermissions也无法调起系统权限提示
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            int grantResult = grantResults[i];
            String permission = permissions[i];
            if (grantResult != PackageManager.PERMISSION_GRANTED &&
                    shouldShowRequestPermissionRationale(permission)) {// 用户禁止了该权限，但仍允许下次提示，此时应该向用户解释为啥需要该权限
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("需要此权限以实现换肤")
                        .setMessage(permission)
                        .setNegativeButton("不需要", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("重新选择", (dialog, which) -> requestPermissions(new String[]{permission}, 0))
                        .setCancelable(false)
                        .create();
                alertDialog.show();
            }
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.activity_btn) {
            if (SkinManager.INSTANCE.isDefaultSkin()) {
                // /storage/emulated/0/Android/data/com.gxd.skinloader.sample/files/black.skin
                File skinFile = new File(getExternalFilesDir(null), "black.skin");
                SkinManager.INSTANCE.load(skinFile, new SkinListener() {
                    @Override
                    public void onSucceed() {
                        button.setText("黑色主题");
                    }
                });
            } else {
                SkinManager.INSTANCE.restoreDefaultSkin();
                button.setText("白色主题");
            }
        } else {
            TextView textView = new TextView(this);
            textView.setText("dynamic add view");
            textView.setTextSize(30);
            textView.setPadding(20, 20, 20, 20);
            textView.setTextColor(getResources().getColor(R.color.colorText));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            rootView.addView(textView, 0);

            addSkinViewMap(textView, AttrFactory.get(getResources(), AttrFactory.TEXT_COLOR, String.format("@%s", R.color.colorText)));
        }
    }
}