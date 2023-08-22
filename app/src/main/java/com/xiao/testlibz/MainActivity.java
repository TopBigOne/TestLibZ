package com.xiao.testlibz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.xiao.testlibz.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    private Button mBtnGet;
    private Button mBtnPost;
    private TextView mTvResp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initEvent();
    }

    private void initView() {
        mTvResp = binding.tvRespContent;
        mBtnGet = binding.btnRequestGet;
        mBtnPost = binding.btnRequestPost;
    }

    private void initEvent() {
        mBtnGet.setOnClickListener(v -> {
            updateContent(NativeLib.nativeHttpGet());

        });
        mBtnPost.setOnClickListener(v -> {
            updateContent(NativeLib.nativeHttpPost());
        });
    }

    private void updateContent(String content) {
        mTvResp.setText(content);
    }

}