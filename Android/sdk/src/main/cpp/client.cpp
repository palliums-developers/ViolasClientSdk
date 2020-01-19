//
// Created by hunter on 20-1-14.
//
#include <jni.h>
#include <string>
#include <fstream>
#include <violas_sdk.hpp>
#include "client.h"

#define CLASS_METHOD(x) Java_io_violas_sdk_Client_##x

using namespace std;

std::string jstringToString(JNIEnv *env, jstring str) {

    string t;
    const char *chr = env->GetStringUTFChars(str, 0);

    t = chr;
    env->ReleaseStringUTFChars(str, chr);

    return t;
}

const std::string CLS_JNIEXCEPTION = "cn/wisenergy/pi/workflow/JNIException";

void ThrowJNIException(JNIEnv *env, const std::string &errorMsg) {
    jclass e_cls = env->FindClass(CLS_JNIEXCEPTION.c_str());
    if (e_cls == NULL) {
        std::cerr << "find class:" << CLS_JNIEXCEPTION << " error!" << std::endl;
        return;
    }

    int r = env->ThrowNew(e_cls, errorMsg.c_str());
}

extern "C"
{
/*
* Class:     io_violas_sdk_Client
* Method:    createNativeClient
* Signature: ()I
*/
JNIEXPORT jlong JNICALL CLASS_METHOD(createNativeClient_0002dWAKwML8)
        (JNIEnv *env, jobject,
         jstring host,
         jshort port,
         jstring faucetKey,
         jboolean syncWithWallet,
         jstring faucetServer,
         jstring mnemonic) {
//    auto faucet = jstringToString(env, faucetKey);
//
//    ifstream ifs(faucet);
//
//    string key(istream_iterator<char>(ifs), istream_iterator<char>());

    try {
        auto client = Violas::Client::create(jstringToString(env, host),
                                             port,
                                             "",
                                             jstringToString(env, faucetKey),
                                             syncWithWallet,
                                             jstringToString(env, faucetServer),
                                             jstringToString(env, mnemonic));

        //Violas::client_ptr cli(client.release());
    }
    catch (exception &e) {
        ThrowJNIException(env, e.what());
    }


    return 0;
}

}
