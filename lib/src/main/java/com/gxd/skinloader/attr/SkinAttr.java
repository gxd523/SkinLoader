package com.gxd.skinloader.attr;

import android.view.View;

public abstract class SkinAttr {
    /**
     * android:background="@color/colorBackground"里的background
     */
    public String attrName;
    /**
     * R.color.colorBackground的值：2130745655
     */
    public int attrValueRefId;
    /**
     * android:background="@color/colorBackground"里的colorBackground
     */
    public String attrValueRefName;
    /**
     * android:background="@color/colorBackground"里的color
     */
    public String attrValueTypeName;

    public abstract void apply(View view);
}
