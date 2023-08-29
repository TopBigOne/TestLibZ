package com.xiao.testlibz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xiao.testlibz.databinding.ActivityMainBinding;
import com.xiao.testlibz.task.RequestResp;
import com.xiao.testlibz.task.NativeRequestClient;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Button              mBtnHttpsGet;
    private Button              mBtnHttpsGetTwo;
    private Button              mBtnRequestPostNoParameter;
    private Button              mBtnPost;
    private TextView            mTvResp;


    private static final String TAG            = "MainActivity_ : ";
    public static final  String HTTP_GET_URL   = "http://jsonplaceholder.typicode.com/posts";
    public static final  String HTTPS_GET_URL  = "https://api.thecatapi.com/v1/images/search?limit=1";
    public static final  String HTTPS_POST_URL = "http://13.214.227.250:7909/iatod";

    public static final String SIGN_MSG = "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";

    private NativeRequestClient nativeTask;
    RequestResp nativeResp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nativeTask = new NativeRequestClient(this);
        initView();
        initEvent();

    }

    private void initView() {
        mTvResp = binding.tvRespContent;
        mBtnPost = binding.btnRequestPost;
        mBtnHttpsGet = binding.btnRequestHttpsGet;
        mBtnHttpsGetTwo = binding.btnRequestHttpsGetTwo;
        mBtnRequestPostNoParameter = binding.btnRequestPostNoParameter;
    }


    private void initEvent() {
        mBtnPost.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>(16);
            map.put("signMsg", SIGN_MSG);
            map.put("name", "小雅");
            map.put("age", 12);
            nativeTask.startTask(HTTPS_POST_URL, map);
        });

        mBtnHttpsGet.setOnClickListener(v -> {
            nativeTask.startTask(HTTPS_GET_URL);

        });
        mBtnHttpsGetTwo.setOnClickListener(v -> {
            nativeTask.startTask(HTTP_GET_URL);

        });

        mBtnRequestPostNoParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nativeTask.startTask(HTTPS_POST_URL, null);
            }
        });

        nativeTask.setRequestClientListener(new NativeRequestClient.NativeRequestClientListener() {
            @Override
            public void onRequestResp(RequestResp resp) {
                updateContent(resp);
            }
        });
    }

    private void updateContent(RequestResp nativeResp) {
        Log.d(TAG, "updateContent() : thread name is : " + Thread.currentThread().getName());
        if (nativeResp == null) {
            Toast.makeText(this, "nativeResp is Null.", Toast.LENGTH_SHORT).show();
            return;
        }
        int    code   = nativeResp.getCode();
        String result = nativeResp.getResult();
        Log.d(TAG, "updateContent: code   : " + code);
        Log.d(TAG, "updateContent: result : " + result);

        mTvResp.setText(result);
    }

}