#include <jni.h>
#include <string>


#include "JniUtils.h"
#include "web_task.h"

using namespace std;


extern "C" JNIEXPORT jstring JNICALL
Java_com_xiao_testlibz_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_xiao_testlibz_NativeLib_httpGet(JNIEnv *env, jclass clazz) {
    //GET请求
    string url = "http://www.weather.com.cn/data/sk/101280601.html";
    WebTask task;
    task.SetUrl(url.c_str());
    task.SetConnectTimeout(5);
    task.DoGetString();
    if (task.WaitTaskDone() == 0) {
        //请求服务器成功
        string jsonResult = task.GetResultString();
        LOGI("返回的json数据是：%s\n", jsonResult.c_str());

        return env->NewStringUTF(jsonResult.c_str());
    }
    LOGI("网络连接失败\n");
    return env->NewStringUTF("网络连接失败！");

}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_xiao_testlibz_NativeLib_httpPost(JNIEnv *env, jclass clazz) {

    return env->NewStringUTF("post 还没有实现.");
}