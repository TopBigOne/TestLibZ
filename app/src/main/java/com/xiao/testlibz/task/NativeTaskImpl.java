package com.xiao.testlibz.task;


import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xiao.testlibz.NetworkApp;

public final class NativeTaskImpl extends NativeTask {


    /**
     * @param code 失败时，code 是native 传递来的；
     * @param msg  成功和失败公用，json or error msg.
     */
    public void nativeNotify(int code, String msg) {
        Log.i(TAG, "nativeNotify: msg : " + msg);
        Toast.makeText(NetworkApp.getInstance(), msg, Toast.LENGTH_SHORT).show();

        releaseNativeTaskPtr();
    }


    /**
     * step 1:
     *
     * @param url
     * @return init status.
     */
    public int init(String url) {
        Log.i(TAG, "init: step 1:");
        if (TextUtils.isEmpty(url)) {
            return TaskStatus.NULL_URL;
        }

        if (url.startsWith("http") || url.startsWith("https")) {
            taskPtr = NativeLib.nativeInitHttp(url);
            if (taskPtr > 0) {
                Log.d(TAG, "init  success: ");
                return TaskStatus.INIT_SUCCESS;
            }
            return TaskStatus.INIT_FAILURE;
        }
        return TaskStatus.ILLEGAL_HOST;
    }



    public NativeResp realStartTask() {
        Log.i(TAG, "realStartTask: step 2:");
        if (taskPtr <= 0) {
            Log.e(TAG, "realStartTask the taskPtr is less than 0.");
            NativeResp nativeResp = new NativeResp();
            nativeResp.setCode(TaskStatus.NativePointerNULL);
            return nativeResp;

        }
        Log.d(TAG, "realStartTask: ...");
        if (taskType == TaskType.GET) {
            return NativeLib.nativeHttpsGet(taskPtr);
        }
        return NativeLib.nativeHttpsPost(taskPtr);
    }


    public void releaseNativeTaskPtr() {

        Log.i(TAG, "releaseNativeTaskPtr: step 3:");
        if (taskPtr > 0) {
            NativeLib.nativeReleaseHttpRequest(taskPtr);
        }
    }







}
