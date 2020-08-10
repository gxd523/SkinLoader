package com.gxd.skinloader;

/**
 * Created by guoxiaodong on 2020/7/6 11:16
 */
public interface SkinListener {
    default void onStart() {
    }

    default void onSucceed() {
    }

    default void onFailed() {
    }
}
