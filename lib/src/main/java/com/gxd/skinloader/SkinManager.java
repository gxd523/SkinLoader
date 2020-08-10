package com.gxd.skinloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by guoxiaodong on 2020/7/3 16:21
 */
public enum SkinManager {
    INSTANCE;
    public static final String SKIN_PATH = "skin_path";
    private String skinPackageName;
    private Resources mResources;
    private WeakReference<Context> mContextRef;
    private ExecutorService threadExecutor;
    private Set<SkinObserver> observerSet;
    private SharedPreferences sharedPreferences;

    public void init(Context context) {
        mContextRef = new WeakReference<>(context.getApplicationContext());
        threadExecutor = Executors.newSingleThreadExecutor();
        observerSet = new HashSet<>();
        sharedPreferences = context.getSharedPreferences("sp_skin", Context.MODE_PRIVATE);
        String skinPath = sharedPreferences.getString(SKIN_PATH, "");
        if (!TextUtils.isEmpty(skinPath)) {
            load(new File(skinPath));
        }
    }

    public void load(File skinFile) {
        load(skinFile, null);
    }

    public void load(File skinFile, SkinListener skinListener) {
        if (skinListener != null) {
            skinListener.onStart();
        }
        threadExecutor.submit(() -> {
            mResources = loadSkinResource(skinFile);
            if (mResources == null) {
                if (skinListener != null) {
                    skinListener.onFailed();
                }
            } else {
                notifySkinChanged();
                sharedPreferences.edit().putString(SKIN_PATH, skinFile.getAbsolutePath()).apply();
                if (skinListener != null) {
                    skinListener.onSucceed();
                }
            }
        });
    }

    private Resources loadSkinResource(File skinFile) {
        Context context = mContextRef.get();
        if (context == null) {
            return null;
        }
        if (skinFile == null || !skinFile.exists()) {
            Log.d("gxd", "找不到皮肤文件..." + (skinFile != null ? skinFile.getAbsolutePath() : ""));
            return null;
        }

        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(skinFile.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
        if (packageInfo == null) {
            return null;
        }
        skinPackageName = packageInfo.packageName;

        AssetManager assetManager;
        Class<AssetManager> assetManagerClass = AssetManager.class;
        try {
            assetManager = assetManagerClass.newInstance();
            Method addAssetPathMethod = assetManagerClass.getMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, skinFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Resources resources = context.getResources();
        return new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
    }

    public void attach(SkinObserver observer) {
        observerSet.add(observer);
    }

    public void detach(SkinObserver observer) {
        observerSet.remove(observer);
    }

    public void setResources(Resources mResources) {
        this.mResources = mResources;
    }

    public boolean isDefaultSkin() {
        return mResources == null || mResources == mContextRef.get().getResources();
    }

    public void restoreDefaultSkin() {
        mResources = mContextRef.get().getResources();
        sharedPreferences.edit().remove(SKIN_PATH).apply();
        notifySkinChanged();
    }

    private void notifySkinChanged() {
        for (SkinObserver skinObserver : observerSet) {
            skinObserver.onSkinChanged();
        }
    }

    public int getColor(int resId) {
        int colorValue = mContextRef.get().getResources().getColor(resId);
        if (mResources == null || TextUtils.isEmpty(skinPackageName)) {
            return colorValue;
        }

        String resName = mContextRef.get().getResources().getResourceEntryName(resId);
        int trueResId = mResources.getIdentifier(resName, "color", skinPackageName);
        try {
            colorValue = mResources.getColor(trueResId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        return colorValue;
    }

    public Drawable getDrawable(int resId) {
        Drawable drawable = mContextRef.get().getResources().getDrawable(resId);
        if (mResources == null || TextUtils.isEmpty(skinPackageName)) {
            return drawable;
        }
        String resName = mContextRef.get().getResources().getResourceEntryName(resId);
        int drawableResId = mResources.getIdentifier(resName, "drawable", skinPackageName);
        try {
            if (android.os.Build.VERSION.SDK_INT < 22) {
                drawable = mResources.getDrawable(drawableResId);
            } else {
                drawable = mResources.getDrawable(drawableResId, null);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    /**
     * 加载指定资源颜色drawable,转化为ColorStateList，保证selector类型的Color也能被转换。
     * 无皮肤包资源返回默认主题颜色
     */
    public ColorStateList convertToColorStateList(int attrValueRefId) {
        ColorStateList colorList = null;
        String resName = mContextRef.get().getResources().getResourceEntryName(attrValueRefId);

        if (!isDefaultSkin()) {
            int skinColorResId = mResources.getIdentifier(resName, "color", skinPackageName);
            if (skinColorResId != 0) {// 如果皮肤包没有复写该资源，但是需要判断是否是ColorStateList
                try {
                    colorList = mResources.getColorStateList(skinColorResId);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (colorList == null) {
            try {
                colorList = mContextRef.get().getResources().getColorStateList(attrValueRefId);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
        if (colorList == null) {
            colorList = new ColorStateList(new int[1][1], new int[]{mContextRef.get().getResources().getColor(attrValueRefId)});
        }
        return colorList;
    }
}

