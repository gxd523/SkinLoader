package com.gxd.skinloader.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.gxd.skinloader.SkinManager;

public class BackgroundAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if ("color".equals(attrValueTypeName)) {
            view.setBackgroundColor(SkinManager.INSTANCE.getColor(attrValueRefId));
        } else if ("drawable".equals(attrValueTypeName)) {
            Drawable drawable = SkinManager.INSTANCE.getDrawable(attrValueRefId);
            view.setBackground(drawable);
        }
    }
}
