package com.xiao.testlibz.task;

public  class RequestResp {
    // 0： 成功，其他值，失败
    private int    code = -1;
    // 成功时：正常数据，失败时：错误日志
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
