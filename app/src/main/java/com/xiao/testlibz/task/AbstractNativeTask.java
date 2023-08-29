package com.xiao.testlibz.task;

import android.util.Log;

import java.util.Map;


public abstract class AbstractNativeTask {
    public static final String TAG = "NativeTask : ";

    protected TaskType taskType = TaskType.GET;
    public    long     taskPtr  = 0;

    public static final String NATIVE_TASK_RESULT_CODE = "native_task_result_code";
    public static final String NATIVE_TASK_RESULT_STR  = "native_task_result_str";
    public static final String NATIVE_TASK_URL         = "native_task_url";

    enum TaskType {
        GET, POST,
    }

    public static class TaskStatus {
        public static final int NULL_URL          = 0;
        public static final int ILLEGAL_HOST      = 1;
        public static final int INIT_SUCCESS      = 2;
        public static final int INIT_FAILURE      = 4;
        public static final int NativePointerNULL = 3;
        public static final int NETWORK_FAILURE   = 6;
    }

    public abstract int init(String url, String parameters);

    public abstract RequestResp realStartTask();

    public abstract void releaseNativeTaskPtr();

    public abstract String generateParameters(Map<String, Object> parameters);


    /**
     * GET request
     *
     * @param url
     * @return
     */
    public RequestResp startTask(String url) {
        return startTask(url, null);
    }


    /**
     * POST request
     *
     * @param url
     * @param parameters
     * @return
     */
    public RequestResp startTask(String url, Map<String, Object> parameters) {
        Log.d(TAG, "startTask: thread is " + Thread.currentThread().getName());
        // step 1:
        if (init(url, generateParameters(parameters)) != TaskStatus.INIT_SUCCESS) {
            Log.d(TAG, "startTask step is in failure");
            RequestResp resp = new RequestResp();
            resp.setCode(TaskStatus.INIT_FAILURE);
            return resp;
        }
        this.taskType = parameters == null ? TaskType.GET : TaskType.POST;
        // step 2:
        RequestResp nativeResp = realStartTask();
        // step 3
        releaseNativeTaskPtr();
        return nativeResp;
    }


    public long cancelRequest() {
        if (taskPtr > 0) {
            NativeLib.nativeCancelHttpRequest(taskPtr);
        }
        return TaskStatus.NativePointerNULL;
    }


}
