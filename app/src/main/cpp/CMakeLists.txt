
cmake_minimum_required(VERSION 3.22.1)

project("testlibz")

add_library(testlibz
        SHARED
        native-lib.cpp
        web_task.cpp
        JniUtils.cpp
        )

target_include_directories(
        "testlibz"
        PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}/include/openssl
        ${CMAKE_CURRENT_SOURCE_DIR}/include/curl
        ${CMAKE_CURRENT_SOURCE_DIR}/include/zlib
)


# libcrypto
add_library(libcrypto SHARED IMPORTED)
set_target_properties(libcrypto PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/jniLibs/${CMAKE_ANDROID_ARCH_ABI}/libcrypto.so)

# openssl
add_library(libssl SHARED IMPORTED)
set_target_properties(libssl PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/jniLibs/${CMAKE_ANDROID_ARCH_ABI}/libssl.so)

# curl
add_library(libcurl SHARED IMPORTED)
set_target_properties(libcurl PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/jniLibs/${CMAKE_ANDROID_ARCH_ABI}/libcurl.so)

# libz
add_library(libz STATIC IMPORTED)
set_target_properties(libz PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/jniLibs/${CMAKE_ANDROID_ARCH_ABI}/libz.a)



target_link_libraries(
        ${CMAKE_PROJECT_NAME}
        libcrypto
        libz
        libssl
        libcurl
        android
        log)