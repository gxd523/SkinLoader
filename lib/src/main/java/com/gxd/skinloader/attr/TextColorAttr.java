package com.gxd.skinloader.attr;

import android.view.View;
import android.widget.TextView;

import com.gxd.skinloader.SkinManager;

public class TextColorAttr extends AbsSkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof TextView && "color".equals(attrValueResTypeName)) {
            ((TextView) view).setTextColor(SkinManager.INSTANCE.convertToColorStateList(attrValueResId));
        }
    }
}
