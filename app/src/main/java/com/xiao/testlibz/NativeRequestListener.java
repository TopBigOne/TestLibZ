package com.xiao.testlibz;

public interface NativeRequestListener {

    void onSuccess(String msg);

    void onFailure(int code, String msg);
}
