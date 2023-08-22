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
    string url = "http://jsonplaceholder.typicode.com/posts";
    WebTask task;
    task.SetUrl(url.c_str());
    task.SetConnectTimeout(10);
    task.DoGetString();
    if (task.WaitTaskDone() == 0) {
        //请求服务器成功
        string jsonResult = task.GetResultString();
        LOGI("返回的json数据是：%s\n", jsonResult.c_str());

        return env->NewStringUTF(jsonResult.c_str());
    }
    LOGE("httpGet ： 网络连接失败\n");
    return env->NewStringUTF("网络连接失败！");

}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_xiao_testlibz_NativeLib_httpPost(JNIEnv *env, jclass clazz) {

    return env->NewStringUTF("post 还没有实现.");
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_xiao_testlibz_NativeLib_httpsGet(JNIEnv *env, jclass clazz) {
    //GET请求
    string url = "https://api.thecatapi.com/v1/images/search?limit=1";
    WebTask task;
    task.SetUrl(url.c_str());
    task.SetConnectTimeout(10);
    task.DoGetString();
    if (task.WaitTaskDone() == 0) {
        //请求服务器成功
        string jsonResult = task.GetResultString();
        LOGI("返回的json数据是：%s\n", jsonResult.c_str());

        return env->NewStringUTF(jsonResult.c_str());
    }
    LOGE("httpsGet ： 网络连接失败\n");
    return env->NewStringUTF("网络连接失败！");

}