package com.yan.correspond

interface CorrespondInvoker {
    fun <T> invoke(tag: String, vararg param: Any?): T?
}