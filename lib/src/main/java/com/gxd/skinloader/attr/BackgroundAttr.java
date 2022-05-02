package com.gxd.skinloader.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.gxd.skinloader.SkinManager;

public class BackgroundAttr extends AbsSkinAttr {
    @Override
    public void apply(View view) {
        if ("color".equals(attrValueResTypeName)) {
            view.setBackgroundColor(SkinManager.INSTANCE.getColor(attrValueResId));
        } else if ("drawable".equals(attrValueResTypeName)) {
            Drawable drawable = SkinManager.INSTANCE.getDrawable(attrValueResId);
            view.setBackground(drawable);
        }
    }
}
