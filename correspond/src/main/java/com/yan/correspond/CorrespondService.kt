package com.yan.correspond

import android.app.Service
import android.content.Intent
import android.os.IBinder

class CorrespondService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { CorrespondInvokeMgr.instance.interceptIntentBack(intent) }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}