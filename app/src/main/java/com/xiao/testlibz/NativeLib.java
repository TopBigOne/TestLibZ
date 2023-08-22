package com.xiao.testlibz;

public class NativeLib {

    static {
        System.loadLibrary("testlibz");
    }

    private static native String httpGet();

    private static native String httpPost();


    public static String nativeHttpGet() {
        return httpGet();
    }

    public static String nativeHttpPost() {
        return httpPost();
    }

}
