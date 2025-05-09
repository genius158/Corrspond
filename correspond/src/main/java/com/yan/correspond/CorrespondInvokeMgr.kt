package com.yan.correspond

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.DeadObjectException
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import com.yan.correspond.CorrespondConstants.SERVICE_CONNECT_TIMEOUT
import java.lang.IllegalArgumentException
import java.util.concurrent.ConcurrentHashMap

/**
 * 管理跨账号调用
 */
class CorrespondInvokeMgr {

    companion object {
        private const val TAG = "CorrespondInvokeMgr"
        private const val EXTRA_CORRESPOND = "ExtraInvokeCorrespond"
        private const val EXTRA_CLASS = "ExtraInvokeClass"

        @JvmStatic
        val instance by lazy { CorrespondInvokeMgr() }

        /**
         * 监听远端进程死亡
         */
        @JvmStatic
        fun linkToDead(correspondInvoker: CorrespondInvoker, onDead: (() -> Unit)) {
            if (correspondInvoker is CrossInvokeProxy) {
                correspondInvoker.remote?.let {
                    BinderDeadLinker.link(it.asBinder(), onDead)
                }
            }
        }
    }

    private val lockSet = HashSet<String>()
    private val remoteMap = ConcurrentHashMap<String, CorrespondInvokeApi>()

    fun getCrossInvoke(
        ctx: Context, clazz: Class<*>, onBind: CorrespondInvokeOnBind? = null
    ): CorrespondInvoker {
        return getCrossInvoke(ctx, clazz.name, false, onBind)
    }

    fun syncGetCrossInvoke(
        ctx: Context, clazz: Class<*>, onBind: CorrespondInvokeOnBind? = null
    ): CorrespondInvoker {
        return getCrossInvoke(ctx, clazz.name, true, onBind)
    }

    fun syncGetCrossInvoke(
        ctx: Context, className: String, onBind: CorrespondInvokeOnBind? = null
    ): CorrespondInvoker {
        return getCrossInvoke(ctx, className, true, onBind)
    }

    /**
     * @param className 承载服务能力的class
     * @param sync 尝试同步获取
     * @param correspondInvokeOnBind 服务绑定成功后，会回调该方法
     */
    private fun getCrossInvoke(
        ctx: Context,
        className: String,
        sync: Boolean,
        correspondInvokeOnBind: CorrespondInvokeOnBind?
    ): CorrespondInvoker {
        val onBindCallback = { invoker: CorrespondInvokeApi?, isCache: Boolean ->
            Log.i(TAG, "getCrossInvoke isCache: $invoker $isCache")
            if (!isCache && invoker != null) {
                correspondInvokeOnBind?.onBind(invoker)
            }
        }

        // 存在本地缓存直接返回
        remoteMap[className]?.let {
            Log.i(TAG, "getCrossInvoke from cache  $className")
            return CrossInvokeProxy(ctx, className, onBindCallback).apply {
                remote = it
            }
        }

        Log.i(TAG, "getCrossInvoke: remote  $className")
        return CrossInvokeProxy(ctx, className, onBindCallback).also { invokeProxy ->
            getServerInvoker(ctx, className, sync) { invoker, isCache ->
                onBindCallback.invoke(invoker, isCache)
                invokeProxy.remote = invoker
            }
        }
    }

    private fun getRealInvoker(className: String): CorrespondInvokeApi? {
        remoteMap[className]?.let { return it }
        synchronized(remoteMap) {
            remoteMap[className]?.let { return it }
            val api = try {
                Log.i(TAG, "findRealInvoker: create remote $className")
                Class.forName(className).getConstructor().newInstance()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            if (api is CorrespondInvokeApi.Stub) {
                remoteMap[className] = api
                return api
            } else {
                throw IllegalArgumentException("$className must instance of CorrespondInvokeApi.Stub")
            }
        }
    }

    /**
     * 获取远程服务实例
     */
    private fun getServerInvoker(
        ctx: Context,
        className: String,
        sync: Boolean,
        callback: (CorrespondInvokeApi?, isCache: Boolean) -> Unit,
    ) {
        remoteMap[className]?.let {
            callback.invoke(it, true)
            return
        }
        if (sync) {
            synchronized(className.asMonitor()) {
                remoteMap[className]?.let {
                    callback.invoke(it, true)
                    return
                }
                if (lockSet.contains(className)) {
                    className.asMonitor().wait(SERVICE_CONNECT_TIMEOUT)
                }
                Log.i(TAG, "getRemoteInvoker syncSet: $lockSet  $remoteMap")

                onWork{
                    queryServerInvoker(ctx, className, true, callback)
                }
                lockSet.add(className)
                className.asMonitor().wait(SERVICE_CONNECT_TIMEOUT)
            }
        } else {
            queryServerInvoker(ctx, className, false, callback)
        }
        Log.i(TAG, "onCorrespondFeedback  remote:  called: " + Thread.currentThread())
    }

    private fun onWork(runnable: Runnable){
        Dispatcher.onWork(runnable)
    }

    private fun queryServerInvoker(
        ctx: Context,
        className: String,
        sync: Boolean,
        callback: (CorrespondInvokeApi?, isCache: Boolean) -> Unit
    ) {
        val trace = getTrace()
        val calledTime = SystemClock.uptimeMillis()

        Log.i(TAG, "getRemoteInvoker: -----")
        val intent = Intent(CorrespondConstants.CORRESPOND_SERVICE_ACTION)
        intent.setPackage(ctx.packageName)
        val extra = Bundle()
        extra.putBinder(EXTRA_CORRESPOND, object : CorrespondInvokeCallback.Stub() {
            override fun onCorrespondFeedback(remote: CorrespondInvokeApi?) {
                checkExecuteTime(calledTime, trace)
                if (sync) {
                    synchronized(className.asMonitor()) {
                        remote?.let { remoteMap[className] = remote }
                        lockSet.remove(className)
                        callback.invoke(remote, false)
                        className.asMonitor().notifyAll()
                    }
                } else {
                    remote?.let { remoteMap[className] = remote }
                    callback.invoke(remote, false)
                }
                linkRemoteDead(remote, ctx, className, callback)
            }
        })
        intent.putExtra(EXTRA_CORRESPOND, extra)
        intent.putExtra(EXTRA_CLASS, className)
        ctx.startService(intent)
        checkExecuteTime(calledTime, trace)
    }

    /**
     * 执行时间过长提醒
     */
    private fun checkExecuteTime(calledTime: Long, trace: String?) {
        if (SystemClock.uptimeMillis() - calledTime >= SERVICE_CONNECT_TIMEOUT) {
            Log.e(TAG, "checkExecuteTime: ${Thread.currentThread()}  $trace")
        }
    }

    private fun linkRemoteDead(
        remote: CorrespondInvokeApi?,
        ctx: Context,
        className: String,
        callback: (CorrespondInvokeApi?, isCache: Boolean) -> Unit
    ) {
        // 如果进程死亡，尝试重新获取服务
        BinderDeadLinker.link(remote?.asBinder()) {
            Log.i(TAG, "linkRemoteDead $remote")
            remoteMap.remove(className)
            getServerInvoker(ctx, className, false, callback)
        }
    }

    /**
     * service端调用
     */
    fun interceptIntentBack(intent: Intent) {
        val bundle = intent.getBundleExtra(EXTRA_CORRESPOND)
        Log.i(TAG, "hookIntentBack extra: $bundle  ${Thread.currentThread()}")
        bundle ?: return

        val correspondBinder = bundle.getBinder(EXTRA_CORRESPOND)
        Log.i(TAG, "hookIntentBack: $correspondBinder  ${intent.getStringExtra(EXTRA_CLASS)}")
        correspondBinder ?: return
        val invokeServerName = intent.getStringExtra(EXTRA_CLASS) ?: return
        val cur = SystemClock.uptimeMillis()
        val correspondInvokerCallback = CorrespondInvokeCallback.Stub.asInterface(correspondBinder)
        correspondInvokerCallback.onCorrespondFeedback(getRealInvoker(invokeServerName))
        Log.i(TAG, "hookIntentBack  duration: ${SystemClock.uptimeMillis() - cur}")
    }

    fun proxyInvokeBinder(obj: Any?): CorrespondInvoker? {
        if (obj is IBinder) {
            return CrossInvokeProxy(null, null, null).apply {
                remote = CorrespondInvokeApi.Stub.asInterface(obj)
            }
        }
        return null
    }

    inner class CrossInvokeProxy(
        private val ctx: Context?,
        private val className: String?,
        private val callback: ((CorrespondInvokeApi?, isCache: Boolean) -> Unit)?,
    ) : CorrespondInvoker {
        var isDead = false
        var remote: CorrespondInvokeApi? = null

        var lastQueryRemote = SystemClock.uptimeMillis()

        private val reconnectAble get() = ctx != null && className != null
        override fun <T> invoke(tag: String, vararg param: Any?): T? {
            // 如果远端已经死亡，且无法重连，不再执行调用
            if (isDead && !reconnectAble) {
                return null
            }
            if (remote == null) {
                Log.e(TAG, "invoke error remote is none")
                // remote为空时 长时间没有再次请求remote，触发异常tryReconnectRemote
                if (SystemClock.uptimeMillis() - lastQueryRemote > SERVICE_CONNECT_TIMEOUT * 3) {
                    lastQueryRemote = SystemClock.uptimeMillis()
                    tryReconnectRemote()
                }
            } else {
                try {
                    return remote!!.invoke(tag, *param)
                } catch (e: RuntimeException) {
                    e.printStackTrace()
                    tryResetRemote(e)
                }
            }
            return null
        }

        private fun tryResetRemote(e: RuntimeException) {
            // 进程死亡，对应的remote需要重新设置
            if (e.cause is DeadObjectException) {
                className?.let { remoteMap.remove(it) }
                tryReconnectRemote()
                isDead = true
            }
        }

        private fun tryReconnectRemote() {
            if (className != null && ctx != null) {
                getServerInvoker(ctx, className, true) { invoker, isCache ->
                    remote = invoker
                    callback?.invoke(invoker, isCache)
                }
            }
        }
    }

    private fun String.asMonitor(): java.lang.Object {
        return intern() as java.lang.Object
    }

    private inline fun getTrace(): String? {
        return if (BuildConfig.DEBUG) {
            Log.getStackTraceString(RuntimeException("${Thread.currentThread()}"))
        } else {
            null
        }
    }

}

fun interface CorrespondInvokeOnBind {
    fun onBind(invoker: CorrespondInvoker)
}