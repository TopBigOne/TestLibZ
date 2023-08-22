#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_xiao_testlibz_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_xiao_testlibz_NativeLib_httpGet(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF("get 还没有实现.");
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_xiao_testlibz_NativeLib_httpPost(JNIEnv *env, jclass clazz) {

    return env->NewStringUTF("post 还没有实现.");
}