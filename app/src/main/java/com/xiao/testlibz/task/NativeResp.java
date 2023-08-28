package com.xiao.testlibz.task;

public final class NativeResp {
    private int code;
    private String result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NativeResp{" +
                "code=" + code +
                ", result='" + result + '\'' +
                '}';
    }
}
