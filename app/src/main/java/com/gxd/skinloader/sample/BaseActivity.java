package com.gxd.skinloader.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.gxd.skinloader.SkinInflaterFactory;
import com.gxd.skinloader.SkinManager;
import com.gxd.skinloader.SkinObserver;
import com.gxd.skinloader.attr.AbsSkinAttr;

import java.util.Arrays;
import java.util.List;

/**
 * Created by guoxiaodong on 2020/7/6 13:31
 */
public abstract class BaseActivity extends Activity implements SkinObserver {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().setFactory(new SkinInflaterFactory());
    }

    @Override
    protected void onResume() {
        super.onResume();
        SkinManager.INSTANCE.attach(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SkinManager.INSTANCE.detach(this);
    }

    @Override
    public void onSkinChanged() {
        LayoutInflater.Factory factory = getLayoutInflater().getFactory();
        if (factory instanceof SkinInflaterFactory) {
            ((SkinInflaterFactory) factory).applySkin();
        }
    }

    public void addSkinViewMap(View view, AbsSkinAttr... skinAttrs) {
        List<AbsSkinAttr> skinAttrList = Arrays.asList(skinAttrs);
        LayoutInflater.Factory factory = getLayoutInflater().getFactory();
        if (factory instanceof SkinInflaterFactory) {
            ((SkinInflaterFactory) factory).addSkinView(view, skinAttrList);
        }
    }
}
