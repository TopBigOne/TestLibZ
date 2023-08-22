package com.xiao.testlibz;

public class NativeLib {

    static {
        System.loadLibrary("testlibz");
    }

    private static native String httpGet();

    private static native String httpPost();

    private static native String httpsGet();


    public static String nativeHttpGet() {
        return httpGet();
    }

    /**
     * https Get
     *
     * @return
     */
    public static String nativeHttpsGet() {
        return httpsGet();
    }

    public static String nativeHttpPost() {
        return httpPost();
    }

}
