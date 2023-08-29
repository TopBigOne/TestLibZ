package com.xiao.testlibz.task;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public final class NativeTaskImpl extends AbstractNativeTask {

    /**
     * step 1:
     *
     * @param url
     * @return init status.
     */
    public int init(String url, String parameters) {
        Log.i(TAG, "init: step 1:");
        if (TextUtils.isEmpty(url)) {
            return TaskStatus.NULL_URL;
        }

        if (url.startsWith("http") || url.startsWith("https")) {
            taskPtr = NativeLib.nativeInitHttp(url, parameters);
            if (taskPtr > 0) {
                Log.d(TAG, "init  success: ");
                return TaskStatus.INIT_SUCCESS;
            }
            return TaskStatus.INIT_FAILURE;
        }
        return TaskStatus.ILLEGAL_HOST;
    }


    public RequestResp realStartTask() {
        Log.i(TAG, "realStartTask: step 2:");
        if (taskPtr <= 0) {
            Log.e(TAG, "realStartTask the taskPtr is less than 0.");
            RequestResp nativeResp = new RequestResp();
            nativeResp.setCode(TaskStatus.NativePointerNULL);
            return nativeResp;

        }
        if (taskType == TaskType.GET) {
            return NativeLib.nativeHttpsGet(taskPtr);
        }
        return NativeLib.nativeHttpsPost(taskPtr);
    }


    public String generateParameters(Map<String, Object> parameters) {
        JSONObject jsonObject = new JSONObject();
        if (parameters == null) {
            return "";
        }
        for (Map.Entry<String, Object> parameterObject : parameters.entrySet()) {
            String tempKey   = parameterObject.getKey();
            Object tempValue = parameterObject.getValue();
            if (TextUtils.isEmpty(tempKey)) {
                continue;
            }
            try {
                jsonObject.put(tempKey, tempValue);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
        return jsonObject.toString();
    }


    public void releaseNativeTaskPtr() {
        Log.i(TAG, "releaseNativeTaskPtr: step 3:");
        if (taskPtr > 0) {
            NativeLib.nativeReleaseHttpRequest(taskPtr);
        }
    }


}
