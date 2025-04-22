package com.yan.correspond

import android.os.IBinder

object BinderDeadLinker {
    fun link(binder: IBinder?, callback: (() -> Unit)?) {
        binder ?: return
        binder.linkToDeath({ callback?.invoke() }, 0)
    }
}