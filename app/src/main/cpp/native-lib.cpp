#include <jni.h>
#include <string>
#include "reader.h"

#include "JniUtils.h"
#include "web_task.h"

using namespace std;


#define JAVA_NATIVE_REQUEST_LISTENER "com/xiao/testlibz/NativeTaskImpl$NativeRequestListener"
#define JAVA_NATIVE_TASK_IMPL_PATH    "com/xiao/testlibz/NativeTaskImpl"

void startNotifyApp(JNIEnv *pEnv, const string &basicString);

/**
 * 通知
 * @param pEnv
 * @param code
 * @param basicString
 */
void startNotifyApp(JNIEnv *pEnv, int code, const string &basicString) {
    LOGI("startNotifyApp : ");

    jclass nativeTaskClass = pEnv->FindClass(JAVA_NATIVE_TASK_IMPL_PATH);
    jobject nativeTaskObject = pEnv->AllocObject(nativeTaskClass);


    jmethodID notifyMethodId = pEnv->GetMethodID(nativeTaskClass, "nativeNotify",
                                                 "(ILjava/lang/String;)V");
    pEnv->CallVoidMethod(nativeTaskObject, notifyMethodId, code,
                         pEnv->NewStringUTF(basicString.c_str()));
}


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
JNIEXPORT jlong JNICALL
Java_com_xiao_testlibz_NativeLib_initHttp(JNIEnv *env, jclass clazz, jstring url) {
    auto task = new WebTask();
    const char *tempUrl = env->GetStringUTFChars(url, JNI_FALSE);
    LOGI("initHttp ： tempUrl : %s", tempUrl);
    task->SetUrl(tempUrl);
    task->SetConnectTimeout(TIMEOUT_10);
    env->ReleaseStringUTFChars(url, tempUrl);
    auto tempLong = reinterpret_cast<uint64_t>(task);
    LOGI("initHttp ： tempLong : %ld", tempLong);
    return tempLong;
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_xiao_testlibz_NativeLib_httpPost(JNIEnv *env, jclass clazz, jlong webTaskPtr) {
    // todo...
    startNotifyApp(env, CURL_SOCKOPT_ERROR, "post 还没有实现");;
    return -1;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_xiao_testlibz_NativeLib_httpsGet(JNIEnv *env, jclass clazz, jlong webTaskPtr) {
    auto *task = reinterpret_cast<WebTask *>(webTaskPtr);
    task->DoGetString();
    if (task->WaitTaskDone() == 0) {
        //请求服务器成功
        string jsonResult = task->GetResultString();
        startNotifyApp(env, CURLE_OK,jsonResult);
        LOGI("httpsGet：%s\n", jsonResult.c_str());
        startParseHttpsGetResp(jsonResult);
        return CURLIOE_OK;
    }
    LOGE("httpsGet ： 网络连接失败\n");
    startNotifyApp(env, CURL_SOCKOPT_ERROR, "网络连接失败");
    return CURL_SOCKOPT_ERROR;
}


extern "C"
JNIEXPORT jlong JNICALL
Java_com_xiao_testlibz_NativeLib_cancelHttpRequest(JNIEnv *env, jclass clazz, jlong ptr) {
    auto *task = reinterpret_cast<WebTask *>(ptr);
    if (task == nullptr) {
        return -1;
    }
    // todo  需要 查看正常条件下 cancelRequest 的CURLcode是多少。
    task->cancelRequest();
    return 0;
}