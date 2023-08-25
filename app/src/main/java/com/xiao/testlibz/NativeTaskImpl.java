package com.xiao.testlibz;


import android.util.Log;
import android.widget.Toast;

public class NativeTaskImpl extends NativeTask {
    private NativeRequestListener nativeRequestListener;

    public void setNativeRequestListener(NativeRequestListener nativeRequestListener) {
        this.nativeRequestListener = nativeRequestListener;
    }

    /**
     * @param code 失败时，code 是native 传递来的；
     * @param msg  成功和失败公用，json or error msg.
     */
    public void nativeNotify(int code, String msg) {
        Log.i(TAG, "nativeNotify: msg : " + msg);
        Toast.makeText(NetworkApp.getInstance(), msg, Toast.LENGTH_SHORT).show();

      /*  if (nativeRequestListener == null) {
            return;
        }
        if (code == TaskStatus.NATIVE_TASK_SUCCESS) {
            nativeRequestListener.onSuccess(msg);
            return;
        }
        nativeRequestListener.onFailure(code, msg);*/
    }

    /**
     * todo 参数需要联调
     *
     * @param url
     * @param taskType
     * @return
     */
    @Override
    public long startTask(String url, TaskType taskType) {
        // Step 1:
        if (init(url) != TaskStatus.INIT_SUCCESS) {
            Log.d(TAG, "startTask step ok");
            return TaskStatus.INIT_SUCCESS;
        }
        this.taskType = taskType;
        // step 2:
        return realStartTask();
    }

    /**
     * todo 参数需要联调
     *
     * @param url
     * @return
     */
    @Override
    public long startTask(String url) {
        taskType = TaskType.GET;
        return startTask(url, taskType);
    }


}
