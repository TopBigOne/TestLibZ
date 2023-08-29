package com.xiao.testlibz.task;




import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.HashMap;
import java.util.Map;

public class RequestWorker extends Worker {

    private static final String TAG = "NativeTaskWork : ";
    private final NativeTaskImpl nativeTask;

    public RequestWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        nativeTask = new NativeTaskImpl();
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: thread is : " + Thread.currentThread().getName());

        Data                inputData   = getInputData();
        Map<String, Object> keyValueMap = inputData.getKeyValueMap();
        String              url         = (String) keyValueMap.get(AbstractNativeTask.NATIVE_TASK_URL);
        Map<String, Object> parameters  = new HashMap<>(keyValueMap);

        parameters.remove(AbstractNativeTask.NATIVE_TASK_URL);

        RequestResp         nativeResp   = parameters.isEmpty() ? nativeTask.startTask(url) : nativeTask.startTask(url, parameters);
        Map<String, Object> parameterMap = new HashMap<>();
        Data.Builder        dataBuilder  = new Data.Builder();
        parameterMap.put(AbstractNativeTask.NATIVE_TASK_RESULT_CODE, nativeResp.getCode());
        parameterMap.put(AbstractNativeTask.NATIVE_TASK_RESULT_STR, nativeResp.getResult());
        dataBuilder.putAll(parameterMap);
        return Result.success(dataBuilder.build());
    }

}

