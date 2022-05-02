package com.gxd.skinloader.attr;

import android.view.View;

public abstract class AbsSkinAttr {
    /**
     * android:background="@color/colorBackground"里的background
     */
    public String attrName;
    /**
     * R.color.colorBackground的resId：2130745655
     */
    public int attrValueResId;
    /**
     * android:background="@color/colorBackground"里的colorBackground
     */
    public String attrValueResEntryName;
    /**
     * android:background="@color/colorBackground"里的color
     */
    public String attrValueResTypeName;

    public abstract void apply(View view);
}
