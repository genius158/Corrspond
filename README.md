# Corrspond
跨进程通用接口实现 demo
直接使用aidl，增删接口会存在兼容问题
service app与client app使用的aidl接口不一致，会导致请求失败甚至崩溃的问题

该demo演示如何通过一个方法涵盖所有的binder接口调用


'''
//所有方法是否待返回值，不定长参数，非明确参数类型，接通过该方法中转
interface CorrespondInvoker {
    fun <T> invoke(tag: String, vararg param: Any?): T?
}

''' 
demo see [TestClient](app/src/main/java/com/yan/correspond/test/TestClient.kt)、 [TestRemote](app/src/main/java/com/yan/correspond/test/TestRemote.kt)