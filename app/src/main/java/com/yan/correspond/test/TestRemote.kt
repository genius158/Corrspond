package com.yan.correspond.test

import android.os.IBinder
import android.util.Log
import com.yan.correspond.CorrespondInvokeApi
import com.yan.correspond.CorrespondInvokeMgr
import com.yan.correspond.CorrespondInvoker

class TestRemote : CorrespondInvokeApi.Stub() {
    companion object {
        private const val TAG = "TestRemote"

        internal const val METHOD_localToRemoteVal = "localToRemoteVal"
        internal const val METHOD_executeWithCallback = "executeWithCallback"
    }

    init {
        Log.i(TAG, "TestRemote : $this")
    }

    override fun <T> invoke(tag: String, vararg param: Any?): T? {
        when (tag) {
            METHOD_localToRemoteVal -> {
                return convertVal(param[0] as String) as T
            }
            METHOD_executeWithCallback -> {
                executeWithCallback(
                    param[0] as String,
                    CorrespondInvokeMgr.instance.proxyInvokeBinder(param[1] as IBinder)
                )
            }
        }
        return null
    }

    private fun convertVal(txt: String?): String {
        Log.i(TAG, "convertVal: -----")
        return "$txt from remote"
    }


    private fun executeWithCallback(txt: String?, callback: CorrespondInvoker?) {
        Log.i(TAG, "executeWithCallback: ----")
        callback?.invoke<Unit>(TestClient.CALLBACK, "$txt from remote")
    }


}