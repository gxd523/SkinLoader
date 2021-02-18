package com.gxd.skinloader.attr;

import android.content.res.Resources;

public class AttrFactory {
    public static final String BACKGROUND = "background";
    public static final String TEXT_COLOR = "textColor";

    public static SkinAttr get(Resources resources, String attrName, String attrValue) {
        if (attrValue.startsWith("@")) {
            try {
                int attrValueRefId = Integer.parseInt(attrValue.substring(1));
                // android:background="@color/colorBackground"里的colorBackground
                String entryName = resources.getResourceEntryName(attrValueRefId);
                // android:background="@color/colorBackground"里的color
                String typeName = resources.getResourceTypeName(attrValueRefId);
                SkinAttr skinAttr = null;
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
                    skinAttr.attrValueRefId = attrValueRefId;
                    skinAttr.attrValueRefName = entryName;
                    skinAttr.attrValueTypeName = typeName;
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
