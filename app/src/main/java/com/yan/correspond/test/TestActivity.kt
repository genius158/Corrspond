package com.yan.correspond.test

import android.app.Activity
import android.os.Bundle
import android.util.Log

class TestActivity : Activity() {
    companion object {
        private const val TAG = "TestActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        val res = TestClient.localToRemoteVal(this, "TestActivityLocal")
        val res1 = TestClient.localToRemoteVal(this, "TestActivityLocal1")
        val res2 = TestClient.localToRemoteVal(this, "TestActivityLocal2")
        val res3 = TestClient.localToRemoteVal(this, "TestActivityLocal3")
        Log.i(TAG, "onCreate  res: $res  $res1  $res2  $res3")

        TestClient.executeWithCallback(this, "TestActivityLocal11") {
            Log.i(TAG, "executeWithCallback onCreate: $it")
        }
        TestClient.executeWithCallback(this, "TestActivityLocal12") {
            Log.i(TAG, "executeWithCallback onCreate: $it")
        }
        TestClient.executeWithCallback(this, "TestActivityLocal13") {
            Log.i(TAG, "executeWithCallback onCreate: $it")
        }
        TestClient.executeWithCallback(this, "TestActivityLocal14") {
            Log.i(TAG, "executeWithCallback onCreate: $it")
        }
    }
}