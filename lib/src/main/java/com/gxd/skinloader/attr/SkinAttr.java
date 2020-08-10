package com.gxd.skinloader.attr;

import android.view.View;

public abstract class SkinAttr {
    /**
     * textColor
     */
    public String attrName;
    /**
     * 2130745655
     */
    public int attrValueRefId;
    /**
     * colorText
     */
    public String attrValueRefName;
    /**
     * color
     */
    public String attrValueTypeName;

    public abstract void apply(View view);
}
