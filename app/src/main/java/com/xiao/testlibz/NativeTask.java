package com.xiao.testlibz;

import android.text.TextUtils;
import android.util.Log;

public abstract class NativeTask {
    public static final String TAG = "NativeTask : ";
    enum TaskType {
        GET, POST,
    }

    static class TaskStatus {
        private static final int NULL_URL = 0;
        private static final int ILLEGAL_HOST = 1;
        public static final int INIT_SUCCESS = 2;
        public static final int INIT_FAILURE = 4;
        public static final int NativePointerNULL = 3;
        public static final int NATIVE_TASK_SUCCESS = 5;
    }

    protected TaskType taskType;
    public long taskPtr = 0;

    /**
     * step 1:
     *
     * @param url
     * @return init status.
     */
    protected int init(String url) {
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

    public long realStartTask() {
        if (taskPtr <= 0) {
            Log.e(TAG, "realStartTask the taskPtr is less than 0.");
            return TaskStatus.NativePointerNULL;

        }
        Log.d(TAG, "realStartTask: ...");
        if (taskType == TaskType.GET) {
            return NativeLib.nativeHttpGet(taskPtr);
        }
        return NativeLib.nativeHttpPost(taskPtr);
    }


    public long cancelRequest() {
        if (taskPtr > 0) {
            return NativeLib.nativeCancelHttpRequest(taskPtr);
        }
        return TaskStatus.NativePointerNULL;
    }


    abstract protected long startTask(String url, TaskType taskType);

    abstract protected long startTask(String url);

}
