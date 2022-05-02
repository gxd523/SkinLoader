package com.gxd.skinloader.sample;

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