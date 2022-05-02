package com.gxd.skinloader.sample;

import android.content.Intent;
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

        button = findViewById(R.id.main_theme_btn);
        button.post(() -> button.setText(String.format("开启%s", SkinManager.INSTANCE.isDefaultSkin() ? "黑色主题" : "白色主题")));
        rootView = findViewById(R.id.activity_main_root);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_theme_btn:
                if (SkinManager.INSTANCE.isDefaultSkin()) {
                    // /storage/emulated/0/Android/data/com.gxd.skinloader.sample/files/black.skin
                    File skinFile = new File(getExternalFilesDir(null), "black.skin");
                    SkinManager.INSTANCE.load(skinFile, new SkinListener() {
                        @Override
                        public void onSucceed() {
                            button.setText("开启白色主题");
                        }
                    });
                } else {
                    SkinManager.INSTANCE.restoreDefaultSkin();
                    button.setText("开启黑色主题");
                }
                break;
            case R.id.main_add_btn:
                TextView textView = new TextView(this);
                textView.setText("dynamic add view");
                textView.setTextSize(30);
                textView.setPadding(20, 20, 20, 20);
                textView.setTextColor(getResources().getColor(R.color.colorText));
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(params);
                rootView.addView(textView, 0);

                addSkinViewMap(textView, AttrFactory.get(getResources(), AttrFactory.TEXT_COLOR, String.format("@%s", R.color.colorText)));
                break;
            case R.id.main_second_btn:
                startActivity(new Intent(this, SecondActivity.class));
                break;
        }
    }
}