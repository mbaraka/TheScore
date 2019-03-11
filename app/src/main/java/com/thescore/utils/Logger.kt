package com.thescore.utils

import android.util.Log
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

fun <R : Any> R.logv(message: String) = Log.v(this.javaClass.simpleName, message)
fun <R : Any> R.logd(message: String, throwable: Throwable? = null) = Log.d(this.javaClass.simpleName, message, throwable)
fun <R : Any> R.logi(message: String) = Log.i(this.javaClass.simpleName, message)
fun <R : Any> R.logw(message: String) = Log.w(this.javaClass.simpleName, message)
fun <R : Any> R.loge(message: String, throwable: Throwable? = null) = Log.e(this.javaClass.simpleName, message, throwable)

fun <R : Any> R.rxError(message: String) = RxHelper.errorAction(this.javaClass.simpleName, message)
fun <R : Any> R.emptyAction(message: String) = Action {Log.i(this.javaClass.simpleName, message)}
fun <R : Any> R.emptyConsumer(message: String) = Consumer<R> {Log.i(this.javaClass.simpleName, message)}
