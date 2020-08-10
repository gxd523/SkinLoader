package com.gxd.skinloader.attr;

import android.view.View;
import android.widget.TextView;

import com.gxd.skinloader.SkinManager;

public class TextColorAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof TextView && "color".equals(attrValueTypeName)) {
            ((TextView) view).setTextColor(SkinManager.INSTANCE.convertToColorStateList(attrValueRefId));
        }
    }
}
