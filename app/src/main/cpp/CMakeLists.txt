
cmake_minimum_required(VERSION 3.22.1)

project("testlibz")

add_library(testlibz
        SHARED
        native-lib.cpp
        web_task.cpp
        JniUtils.cpp
        )

include_directories(
        ${CMAKE_CURRENT_SOURCE_DIR}/include/openssl
        ${CMAKE_CURRENT_SOURCE_DIR}/include/curl
        ${CMAKE_CURRENT_SOURCE_DIR}/include/zlib
)

#
## openssl
add_library(libssl SHARED IMPORTED)
set_target_properties(libssl PROPERTIES IMPORTED_LOCATION_DEBUG ${CMAKE_CURRENT_SOURCE_DIR}/jniLibs/${CMAKE_ANDROID_ARCH_ABI}/libssl.so)

## curl
add_library(libcurl SHARED IMPORTED)
set_target_properties(libcurl PROPERTIES IMPORTED_LOCATION_DEBUG ${CMAKE_CURRENT_SOURCE_DIR}/jniLibs/${CMAKE_ANDROID_ARCH_ABI}/libcurl.so)

## libz
add_library(libz STATIC IMPORTED)
set_target_properties(libz PROPERTIES IMPORTED_LOCATION_DEBUG ${CMAKE_CURRENT_SOURCE_DIR}/jniLibs/${CMAKE_ANDROID_ARCH_ABI}/libz.so)


target_link_libraries(
        ${CMAKE_PROJECT_NAME}

        ${libz}
        ${libssl}
        ${libcurl}
        android
        log)