#include <jni.h>
#include <string>
#include "reader.h"

#include "JniUtils.h"
#include "web_task.h"

using namespace std;

#define JAVA_NATIVE_TASK_RESP_PATH    "com/xiao/testlibz/task/NativeResp"

#define TASK_POINTER_NULL "the WebTask pointer is NULL"
#define TASK_CONNECTION_SERVER_IN_FAILURE "connect the sever in failure"

bool isDebugMode = false;

typedef enum TaskStatus {
    NATIVE_POINTER_NULL,
    NATIVE_CONNECT_NET_FAILURE,

} TaskStatus;



jobject configResp(JNIEnv *pEnv, const int code, const string &basicString);


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

extern "C"
JNIEXPORT jlong JNICALL
Java_com_xiao_testlibz_task_NativeLib_initHttp(JNIEnv *env, jclass clazz, jboolean is_debug,
                                               jstring url, jstring parameters) {

    isDebugMode = is_debug;
    auto task = new WebTask();
    const char *tempUrl = env->GetStringUTFChars(url, JNI_FALSE);
    LOGI("initHttp ： tempUrl        : %s", tempUrl);
    task->SetUrl(tempUrl);
    task->SetConnectTimeout(TIMEOUT_10);
    int pLength = env->GetStringLength(parameters);
    const char *tempParameters = env->GetStringUTFChars(parameters, JNI_FALSE);
    LOGI("initHttp ： pLength        : %d", pLength);
    LOGI("initHttp ： tempParameters : %s", tempParameters);
    if (pLength > 0) {
        curl_slist_append(task->m_headerlist, "Content-Type: application/x-www-form-urlencoded");
        curl_easy_setopt(task->m_curl, CURLOPT_POSTFIELDS, tempParameters);

        Json::Reader reader;
        Json::Value root;
        if (!reader.parse(tempParameters, root)) {
            LOGE("initHttp : parse json in failure.");
        }
        if (root.isObject()) {
            for (const auto &item: root.getMemberNames()) {
                const Json::Value &value = root[item];
                const char *tempKey = item.c_str();
                const char *tempValue;
                switch (value.type()) {
                    case Json::stringValue:
                        tempValue = value.asString().c_str();
                        break;
                    case Json::intValue:
                        tempValue = to_string(value.asInt()).c_str();
                        break;
                    case Json::realValue:
                        tempValue = to_string(value.asDouble()).c_str();
                        break;
                    default:
                        break;

                }
                LOGI("   : tempKey   : %s", tempKey);
                LOGI("   : valueType : %s", tempValue);
                task->AddPostString(tempKey, tempValue);
            }
        }

    }

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
        return configResp(env, CURLE_OK, jsonResult);
    }
    LOGE("httpsGet ： 网络连接失败\n");
    return configResp(env, NATIVE_POINTER_NULL, TASK_CONNECTION_SERVER_IN_FAILURE);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_xiao_testlibz_task_NativeLib_httpsPost(JNIEnv *env, jclass clazz, jlong webTaskPtr) {

    auto *task = reinterpret_cast<WebTask *>(webTaskPtr);
    if (task == nullptr) {
        return configResp(env, NATIVE_POINTER_NULL, TASK_POINTER_NULL);
    }
    task->DoGetString();
    int resultCode = -1;
    if ((resultCode = task->WaitTaskDone()) == 0) {
        // 请求服务器成功
        string jsonResult = task->GetResultString();
        LOGI("httpsPost：success : %s\n", jsonResult.c_str());
        return configResp(env, CURLE_OK, jsonResult);
    }
    LOGE("httpsPost ：resultCode :  %d , %s. \n", resultCode, "网络连接失败");
    return configResp(env, resultCode, TASK_CONNECTION_SERVER_IN_FAILURE);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_xiao_testlibz_task_NativeLib_cancelHttpRequest(JNIEnv *env, jclass clazz, jlong ptr) {
    auto *task = reinterpret_cast<WebTask *>(ptr);
    if (task == nullptr) {
        return;
    }
    // todo  需要 查看正常条件下 CancelRequest 的CURLcode是多少。
    task->CancelRequest();
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