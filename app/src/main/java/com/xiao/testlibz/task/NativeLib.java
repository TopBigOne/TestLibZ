package com.xiao.testlibz.task;

import android.os.Build;

import androidx.core.os.BuildCompat;

import com.xiao.testlibz.BuildConfig;

public final class NativeLib {

    static {
        System.loadLibrary("testlibz");
    }

    private static native long initHttp(boolean isDebug,String url);

    private static native void cancelHttpRequest(long ptr);

    private static native void releaseHttpRequest(long ptr);

    private static native NativeResp httpsGet(long ptr);

    private static native NativeResp httpsPost(long ptr);



    public static long nativeInitHttp(String utl) {
        return initHttp(BuildConfig.DEBUG, utl);
    }

    /**
     * https Get
     *
     * @return
     */
    public static NativeResp nativeHttpsGet(long taskPtr) {
        return httpsGet(taskPtr);
    }
 /**
     * https Get
     *
     * @return
     */
    public static NativeResp nativeHttpsPost(long taskPtr) {
        return httpsPost(taskPtr);
    }


    /**
     * 不需要手动释放ptr
     * @param ptr
     * @return
     */
    public static void nativeCancelHttpRequest(long ptr) {
         cancelHttpRequest(ptr);
    }

    public static void nativeReleaseHttpRequest(long ptr) {
        releaseHttpRequest(ptr);
    }


}
