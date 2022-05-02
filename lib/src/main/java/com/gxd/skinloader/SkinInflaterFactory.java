package com.gxd.skinloader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.gxd.skinloader.attr.AttrFactory;
import com.gxd.skinloader.attr.AbsSkinAttr;

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
    private final Map<View, List<AbsSkinAttr>> skinViewMap = new HashMap<>();

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
        List<AbsSkinAttr> skinAttrList = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);// 例如：layout_width
            String attrValue = attrs.getAttributeValue(i);// resId(例如：@2130771970)、值(例如：-1)

            if (!AttrFactory.isSupportedAttr(attrName)) {
                continue;
            }
            AbsSkinAttr skinAttr = AttrFactory.get(context.getResources(), attrName, attrValue);
            if (skinAttr != null) {
                skinAttrList.add(skinAttr);
            }
        }

        if (skinAttrList.size() > 0) {
            view.post(() -> addSkinView(view, skinAttrList));
        }
    }

    public void addSkinView(View view, List<AbsSkinAttr> skinAttrList) {
        if (skinAttrList == null || skinAttrList.size() == 0) {
            return;
        }
        skinViewMap.put(view, skinAttrList);
        if (!SkinManager.INSTANCE.isDefaultSkin()) {
            for (AbsSkinAttr skinAttr : skinAttrList) {
                skinAttr.apply(view);
            }
        }
    }

    public void applySkin() {
        for (Map.Entry<View, List<AbsSkinAttr>> entry : skinViewMap.entrySet()) {
            View skinView = entry.getKey();
            if (skinView == null) {
                continue;
            }
            for (AbsSkinAttr skinAttr : entry.getValue()) {
                skinAttr.apply(skinView);
            }
        }
    }
}
