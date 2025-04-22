package com.yan.correspond.test

import android.content.Context
import android.util.Log
import com.yan.correspond.CorrespondInvokeApi
import com.yan.correspond.CorrespondInvokeMgr
import com.yan.correspond.CorrespondInvoker
import com.yan.correspond.test.TestRemote.Companion.METHOD_executeWithCallback
import com.yan.correspond.test.TestRemote.Companion.METHOD_localToRemoteVal

object TestClient {
    private const val TAG = "TestClient"
    internal const val CALLBACK = "callback"
    private fun getInvoker(context: Context): CorrespondInvoker {
        return CorrespondInvokeMgr.instance.syncGetCrossInvoke(context, TestRemote::class.java) {
            onBind(context,it)
        }
    }

    private fun onBind(context: Context,invoker: CorrespondInvoker) {
        Log.i(TAG, "onBind: ----  $invoker")
        Log.i(TAG, "onBind: ----  ${localToRemoteVal(context, "test onBind")}")
    }

    fun localToRemoteVal(context: Context, value: String): String? {
        return getInvoker(context).invoke<String>(METHOD_localToRemoteVal, value)
    }

    fun executeWithCallback(context: Context, value: String, callback: (String) -> Unit): String? {
        val remoteCallback = object : CorrespondInvokeApi.Stub() {
            override fun <T> invoke(tag: String, vararg param: Any?): T? {
                callback.invoke(param[0] as String)
                return null
            }
        }
        return getInvoker(context).invoke<String>(METHOD_executeWithCallback, value, remoteCallback)
    }
}