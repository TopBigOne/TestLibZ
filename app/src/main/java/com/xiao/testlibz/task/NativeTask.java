package com.xiao.testlibz.task;

import android.util.Log;

import java.util.Map;


public abstract class NativeTask {
    public static final String TAG = "NativeTask : ";

    protected TaskType taskType = TaskType.GET;
    public    long     taskPtr  = 0;

    enum TaskType {
        GET, POST,
    }

   public static class TaskStatus {
       public static final int NULL_URL            = 0;
       public static final int ILLEGAL_HOST        = 1;
        public static final  int INIT_SUCCESS        = 2;
        public static final  int INIT_FAILURE        = 4;
        public static final  int NativePointerNULL   = 3;
        public static final  int NATIVE_TASK_SUCCESS = 5;
    }

    public  abstract int init(String url);

    public  abstract NativeResp realStartTask();

    public  abstract void releaseNativeTaskPtr();


    /**
     * GET request
     *
     * @param url
     * @return
     */
    public NativeResp startTask(String url) {
        if (init(url) != TaskStatus.INIT_SUCCESS) {
            Log.d(TAG, "startTask step is in failure");
            NativeResp resp = new NativeResp();
            resp.setCode(TaskStatus.INIT_FAILURE);
            return resp;
        }
        this.taskType = TaskType.GET;
        // step 2:
        NativeResp nativeResp = realStartTask();
        // step 3
        releaseNativeTaskPtr();
        return nativeResp;

    }


    /**
     * POST request
     *
     * @param url
     * @param parameters
     * @return
     */
    public NativeResp startTask(String url, Map<String, Object> parameters) {
        // step 1:
        if (init(url) != TaskStatus.INIT_SUCCESS) {
            Log.d(TAG, "startTask step is in failure");
            NativeResp resp = new NativeResp();
            resp.setCode(TaskStatus.INIT_FAILURE);
            return resp;
        }
        this.taskType = TaskType.POST;
        // step 2:
        NativeResp nativeResp = realStartTask();
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


/*
    abstract protected NativeResp startTask(String url, TaskType taskType);

    abstract protected NativeResp startTask(String url);
    */

}
