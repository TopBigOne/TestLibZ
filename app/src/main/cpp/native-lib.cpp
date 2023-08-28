#include <jni.h>
#include <string>
#include "reader.h"

#include "JniUtils.h"
#include "web_task.h"

using namespace std;


#define JAVA_NATIVE_TASK_IMPL_PATH    "com/xiao/testlibz/task/NativeTaskImpl"
#define JAVA_NATIVE_TASK_RESP_PATH    "com/xiao/testlibz/task/NativeResp"

#define TASK_POINTER_NULL "the WebTask pointer is NULL"
#define TASK_CONNECTION_SERVER_IN_FAILURE "connect the sever in failure"

bool isDebugMode = false;

typedef enum TaskStatus {
    NATIVE_POINTER_NULL,
    NATIVE_CONNECT_NET_FAILURE,

} TaskStatus;


void startNotifyApp(JNIEnv *pEnv, const string &basicString);

jobject configResp(JNIEnv *pEnv, const int code, const string &basicString);

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

/**
 * for java.
 * @param pEnv
 * @param code
 * @param basicString
 * @return
 */
jobject configResp(JNIEnv *pEnv, const int code, const string &basicString) {
    LOGI("configResp : ");
    jclass nativeTaskClass = pEnv->FindClass(JAVA_NATIVE_TASK_RESP_PATH);
    jobject respObject = pEnv->AllocObject(nativeTaskClass);
    // setCode
    jmethodID setCodeMethodId = pEnv->GetMethodID(nativeTaskClass, "setCode",
                                                  "(I)V");
    pEnv->CallVoidMethod(respObject, setCodeMethodId, code);
    // setCode
    jmethodID setResultMethodId = pEnv->GetMethodID(nativeTaskClass, "setResult",
                                                    "(Ljava/lang/String;)V");
    pEnv->CallVoidMethod(respObject, setResultMethodId, pEnv->NewStringUTF(basicString.c_str()));
    return respObject;

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
Java_com_xiao_testlibz_task_NativeLib_initHttp(JNIEnv *env, jclass clazz, jboolean is_debug,
                                               jstring url) {

    isDebugMode = is_debug;
    auto task = new WebTask();
    const char *tempUrl = env->GetStringUTFChars(url, JNI_FALSE);
    LOGI("initHttp ： tempUrl : %s", tempUrl);
    task->SetUrl(tempUrl);
    task->SetConnectTimeout(TIMEOUT_10);
    env->ReleaseStringUTFChars(url, tempUrl);
    return reinterpret_cast<jlong>(task);


}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_xiao_testlibz_task_NativeLib_httpsGet(JNIEnv *env, jclass clazz, jlong webTaskPtr) {
    auto *task = reinterpret_cast<WebTask *>(webTaskPtr);
    if (task == nullptr) {
        return configResp(env, NATIVE_POINTER_NULL, TASK_POINTER_NULL);
    }
    task->DoGetString();
    if (task->WaitTaskDone() == 0) {
        // 请求服务器成功
        string jsonResult = task->GetResultString();
        LOGI("httpsGet：%s\n", jsonResult.c_str());
        startParseHttpsGetResp(jsonResult);
        return configResp(env, CURLE_OK, jsonResult);
    }
    LOGE("httpsGet ： 网络连接失败\n");
    return configResp(env, NATIVE_POINTER_NULL, TASK_CONNECTION_SERVER_IN_FAILURE);
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_xiao_testlibz_task_NativeLib_httpsPost(JNIEnv *env, jclass clazz, jlong ptr) {
    return configResp(env, NATIVE_POINTER_NULL, TASK_CONNECTION_SERVER_IN_FAILURE);

}


extern "C"
JNIEXPORT void JNICALL
Java_com_xiao_testlibz_task_NativeLib_cancelHttpRequest(JNIEnv *env, jclass clazz, jlong ptr) {
    auto *task = reinterpret_cast<WebTask *>(ptr);
    if (task == nullptr) {
        return ;
    }
    // todo  需要 查看正常条件下 cancelRequest 的CURLcode是多少。
    task->cancelRequest();
    delete task;
    task = nullptr;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_xiao_testlibz_task_NativeLib_releaseHttpRequest(JNIEnv *env, jclass clazz, jlong ptr) {
    auto *webTask = reinterpret_cast<WebTask *>(ptr);
    if (webTask) {
        delete webTask;
        webTask = nullptr;
    }
}