#include <jni.h>
#include <string>
#include "reader.h"

#include "JniUtils.h"
#include "web_task.h"

using namespace std;

void startParseHttpGetResp(string &respStr) {
    Json::Reader reader;
    Json::Value root;
    if (!reader.parse(respStr, root)) {
        LOGE("startParseHttpGetResp : parse json in failure.");
        return;
    }

    for (const auto &item: root) {
        int userId = item["userId"].asInt();
        int id = item["id"].asInt();
        string title = item["title"].asString();
        string body = item["body"].asString();
        LOGI("  startParseHttpGetResp : ========================================================↓");
        LOGD("  startParseHttpGetResp : userId : %d", userId);
        LOGD("  startParseHttpGetResp : id     : %d", id);
        LOGD("  startParseHttpGetResp : title  : %s", title.c_str());
        LOGD("  startParseHttpGetResp : body   : %s", body.c_str());
        LOGI("  startParseHttpGetResp : ========================================================↑");
    }

}


void startParseHttpsGetResp(string &respStr) {
    Json::Reader reader;
    Json::Value root;
    if (!reader.parse(respStr, root)) {
        LOGE("startParseHttpsGetResp : parse json in failure.");
        return;
    }

    for (const auto &item: root) {
        string id = item["id"].asString();
        string url = item["url"].asString();
        LOGI("  startParseHttpsGetResp : ========================================================↓");
        LOGD("  startParseHttpsGetResp : id  : %s", id.c_str());
        LOGD("  startParseHttpsGetResp : url : %s", url.c_str());
        LOGI("  startParseHttpsGetResp : ========================================================↑");
    }

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
        LOGI("httpGet ：%s\n", jsonResult.c_str());
        startParseHttpGetResp(jsonResult);
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
        LOGI("httpsGet：%s\n", jsonResult.c_str());
        startParseHttpsGetResp(jsonResult);

        return env->NewStringUTF(jsonResult.c_str());
    }
    LOGE("httpsGet ： 网络连接失败\n");
    return env->NewStringUTF("网络连接失败！");

}

