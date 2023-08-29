package com.xiao.testlibz.task;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.Map;
import java.util.function.BiConsumer;

public class NativeRequestClient {
    private static final String TAG = "NativeRequestClient : ";

    private final Activity                   context;
    private final LifecycleOwner             lifecycleOwner;
    private final OneTimeWorkRequest.Builder oneTimeWorkRequestBuilder;

    private NativeRequestClientListener listener;

    public NativeRequestClient(Activity context) {
        oneTimeWorkRequestBuilder = new OneTimeWorkRequest.Builder(RequestWorker.class);
        this.context = context;
        this.lifecycleOwner = (LifecycleOwner) context;
    }

    public void setRequestClientListener(NativeRequestClientListener listener) {
        this.listener = listener;
    }

    /**
     * Get request
     *
     * @param url api
     */
    public void startTask(String url) {
        startTask(url, null);
    }

    /**
     * Post request
     *
     * @param url          api
     * @param parameterMap only for : Map<String, String> , Map<String, Integer> Map<String, Double>
     */
    public void startTask(String url, Map<String, Object> parameterMap) {
        Data.Builder dataBuilder = new Data.Builder();
        if (parameterMap != null && !parameterMap.isEmpty()) {
            dataBuilder.putAll(parameterMap);
        }
        dataBuilder.putString(AbstractNativeTask.NATIVE_TASK_URL, url);
        Data inputData = dataBuilder.build();
        oneTimeWorkRequestBuilder.setInputData(inputData);
        OneTimeWorkRequest oneTimeWorkRequest = oneTimeWorkRequestBuilder.build();

        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueue(oneTimeWorkRequest);
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.getId()).observe(lifecycleOwner, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                    int    code   = workInfo.getOutputData().getInt(AbstractNativeTask.NATIVE_TASK_RESULT_CODE, -1);
                    String result = workInfo.getOutputData().getString(AbstractNativeTask.NATIVE_TASK_RESULT_STR);
                    if (listener != null) {
                        RequestResp resp = new RequestResp();
                        resp.setCode(code);
                        resp.setResult(result);
                        listener.onRequestResp(resp);
                    }
                    Log.d(TAG, "onChanged: thread name is  : " + Thread.currentThread().getName());
                    Log.d(TAG, "onChanged: code            : " + code);
                    Log.d(TAG, "onChanged: result          : " + result);
                }
            }
        });
    }


  public   interface NativeRequestClientListener {
        @MainThread
        void onRequestResp(RequestResp resp);
    }


}

