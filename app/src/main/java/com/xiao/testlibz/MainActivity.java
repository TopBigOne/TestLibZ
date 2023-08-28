package com.xiao.testlibz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xiao.testlibz.databinding.ActivityMainBinding;
import com.xiao.testlibz.task.NativeLib;
import com.xiao.testlibz.task.NativeResp;
import com.xiao.testlibz.task.NativeTask;
import com.xiao.testlibz.task.NativeTaskImpl;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Button              mBtnHttpsGet;
    private Button              mBtnHttpsGetTwo;
    private Button              mBtnPost;
    private TextView            mTvResp;


    private static final String TAG           = "MainActivity_ : ";
    public static final  String HTTP_GET_URL  = "http://jsonplaceholder.typicode.com/posts";
    public static final  String HTTPS_GET_URL = "https://api.thecatapi.com/v1/images/search?limit=1";

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
        mBtnPost = binding.btnRequestPost;
        mBtnHttpsGet = binding.btnRequestHttpsGet;
        mBtnHttpsGetTwo = binding.btnRequestHttpsGetTwo;
    }

    NativeResp nativeResp;

    private void initEvent() {
        mBtnPost.setOnClickListener(v -> {
            //updateContent(NativeLib.nativeHttpPost());
        });

        mBtnHttpsGet.setOnClickListener(v -> {
            nativeResp = nativeTask.startTask(HTTPS_GET_URL);
            updateContent(nativeResp);

        });
        mBtnHttpsGetTwo.setOnClickListener(v -> {
            nativeResp = nativeTask.startTask(HTTP_GET_URL);
            updateContent(nativeResp);

        });


    }

    private void updateContent(NativeResp nativeResp) {
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