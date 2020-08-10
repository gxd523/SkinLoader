package com.gxd.skinloader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.gxd.skinloader.attr.AttrFactory;
import com.gxd.skinloader.attr.SkinAttr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guoxiaodong on 2020/7/6 13:45
 */
public class SkinInflaterFactory implements LayoutInflater.Factory {
    public static final String SKIN_NAMESPACE = "http://schemas.android.com/android/skin";
    public static final String SKIN_ATTR = "skin_enable";
    private Map<View, List<SkinAttr>> skinViewMap = new HashMap<>();

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        boolean isSkinEnable = attrs.getAttributeBooleanValue(SKIN_NAMESPACE, SKIN_ATTR, false);
        if (!isSkinEnable) {
            return null;
        }

        View view = createView(context, name, attrs);

        if (view == null) {
            return null;
        }

        parseSkinAttr(context, attrs, view);

        return view;
    }

    private View createView(Context context, String name, AttributeSet attrs) {
        View result = null;
        try {
            if (-1 == name.indexOf('.')) {
                if ("View".equals(name)) {
                    result = LayoutInflater.from(context).createView(name, "android.view.", attrs);
                }
                if (result == null) {
                    result = LayoutInflater.from(context).createView(name, "android.widget.", attrs);
                }
                if (result == null) {
                    result = LayoutInflater.from(context).createView(name, "android.webkit.", attrs);
                }
            } else {
                result = LayoutInflater.from(context).createView(name, null, attrs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void parseSkinAttr(Context context, AttributeSet attrs, View view) {
        List<SkinAttr> skinAttrList = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);

            if (!AttrFactory.isSupportedAttr(attrName)) {
                continue;
            }
            SkinAttr skinAttr = AttrFactory.get(context.getResources(), attrName, attrValue);
            if (skinAttr != null) {
                skinAttrList.add(skinAttr);
            }
        }

        if (skinAttrList.size() > 0) {
            addSkinView(view, skinAttrList);
        }
    }

    public void addSkinView(View view, List<SkinAttr> skinAttrList) {
        if (skinAttrList == null || skinAttrList.size() == 0) {
            return;
        }
        skinViewMap.put(view, skinAttrList);
        if (!SkinManager.INSTANCE.isDefaultSkin()) {
            for (SkinAttr skinAttr : skinAttrList) {
                skinAttr.apply(view);
            }
        }
    }

    public void applySkin() {
        for (Map.Entry<View, List<SkinAttr>> entry : skinViewMap.entrySet()) {
            View skinView = entry.getKey();
            if (skinView == null) {
                continue;
            }
            for (SkinAttr skinAttr : entry.getValue()) {
                skinAttr.apply(skinView);
            }
        }
    }
}
