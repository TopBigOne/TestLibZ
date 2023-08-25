package com.xiao.testlibz;

public class NativeLib {

    static {
        System.loadLibrary("testlibz");
    }

    private static native long initHttp(String url);

    private static native int httpGet(long ptr);

    private static native int httpPost(long ptr);

    private static native int httpsGet(long ptr);

    private static native long cancelHttpRequest(long ptr);


    public static int nativeHttpGet(long taskPtr) {
        return httpGet(taskPtr);
    }

    /**
     * https Get
     *
     * @return
     */
    public static long nativeHttpsGet(long taskPtr) {
        return httpsGet(taskPtr);
    }

    public static long nativeHttpPost(long ptr) {
        return httpPost(ptr);
    }

    public static long nativeCancelHttpRequest(long ptr) {
        return cancelHttpRequest(ptr);
    }

    public static long nativeInitHttp(String utl) {
        return initHttp(utl);
    }


}
