package com.gxd.skinloader.attr;

import android.content.res.Resources;

public class AttrFactory {
    public static final String BACKGROUND = "background";
    public static final String TEXT_COLOR = "textColor";

    public static AbsSkinAttr get(Resources resources, String attrName, String attrValue) {
        if (attrValue.startsWith("@")) {
            try {
                int attrValueResId = Integer.parseInt(attrValue.substring(1));
                // android:background="@color/colorBackground"里的colorBackground
                String entryName = resources.getResourceEntryName(attrValueResId);
                // android:background="@color/colorBackground"里的color
                String typeName = resources.getResourceTypeName(attrValueResId);
                AbsSkinAttr skinAttr = null;
                switch (attrName) {
                    case BACKGROUND:
                        skinAttr = new BackgroundAttr();
                        break;
                    case TEXT_COLOR:
                        skinAttr = new TextColorAttr();
                        break;
                }

                if (skinAttr != null) {
                    skinAttr.attrName = attrName;
                    skinAttr.attrValueResId = attrValueResId;
                    skinAttr.attrValueResEntryName = entryName;
                    skinAttr.attrValueResTypeName = typeName;
                }
                return skinAttr;
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isSupportedAttr(String attrName) {
        switch (attrName) {
            case BACKGROUND:
            case TEXT_COLOR:
                return true;
        }
        return false;
    }
}
