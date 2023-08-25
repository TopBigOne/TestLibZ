package com.xiao.testlibz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.xiao.testlibz.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Button mBtnGet;
    private Button mBtnHttpsGet;
    private Button mBtnPost;
    private TextView mTvResp;


    private static final String TAG = "MainActivity_ : ";
    public static final String HTTP_GET_URL = "http://jsonplaceholder.typicode.com/posts";
    public static final String HTTPS_GET_URL = "https://api.thecatapi.com/v1/images/search?limit=1";

    private NativeTaskImpl nativeTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nativeTask = new NativeTaskImpl();
        initView();
        initEvent();

    }

    private void initView() {
        mTvResp = binding.tvRespContent;
        mBtnGet = binding.btnRequestGet;
        mBtnPost = binding.btnRequestPost;
        mBtnHttpsGet = binding.btnRequestHttpsGet;
    }

    private void initEvent() {
        mBtnGet.setOnClickListener(v -> {
            Log.d(TAG, "initEvent: ");
            nativeTask.startTask(HTTP_GET_URL);


        });
        mBtnPost.setOnClickListener(v -> {

            //updateContent(NativeLib.nativeHttpPost());
        });

        mBtnHttpsGet.setOnClickListener(v -> {
            nativeTask.startTask(HTTPS_GET_URL);

        });

        nativeTask.setNativeRequestListener(new NativeRequestListener() {
            @Override
            public void onSuccess(String msg) {
                Log.d(TAG, "nativeTask onSuccess: msg   :"+msg);
                updateContent(msg);
            }

            @Override
            public void onFailure(int code, String errMsg) {
                Log.d(TAG, "nativeTask onSuccess: errMsg :"+errMsg);
                updateContent(errMsg);
            }
        });
    }

    private void updateContent(String content) {
        mTvResp.setText(content);
    }

}