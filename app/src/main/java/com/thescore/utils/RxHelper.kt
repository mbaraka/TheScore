package com.thescore.utils

import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import io.reactivex.CompletableTransformer
import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

object RxHelper {

    fun <T> asyncToUiFlowable(): FlowableTransformer<T, T> {
        return FlowableTransformer { observable ->
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> asyncToUiSingle(): SingleTransformer<T, T> {
        return SingleTransformer { single ->
            single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun asyncToUiCompletable(): CompletableTransformer {
        return CompletableTransformer { completable ->
            completable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> asyncToUiObservable(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun unsubscribe(@Nullable disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    @NonNull
    fun errorAction(@NonNull tag: String, @NonNull message: String): Consumer<Throwable> {
        return Consumer { throwable ->
            Log.e(tag, message)
            throwable.printStackTrace()
        }
    }

}
