package com.thescore.home

import android.annotation.SuppressLint
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thescore.com.thescore.model.SortType
import com.thescore.database.AppDatabase
import com.thescore.database.DBHelper
import com.thescore.model.Team
import com.thescore.utils.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.SerialDisposable
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

class HomePresenter @Inject constructor(
    private val requestHelper: RequestHelper,
    private val dbHelper: DBHelper) {

    private var currentData: List<Team> = ArrayList<Team>()
    private val dataEmitter: PublishSubject<List<Team>> = PublishSubject.create()
    private val errorEmitter: PublishSubject<String> = PublishSubject.create()

    private val requestDisposable = SerialDisposable()

    fun loadData() {
        requestDisposable.set(Observable.mergeDelayError(loadFromDB(), requestData())
                .subscribe({
                    currentData = it
                    dataEmitter.onNext(it)
                }, {
                    loge("failed to load data", it)
                    errorEmitter.onNext(it.localizedMessage)
                }))
    }

    fun listenToData(): Observable<List<Team>> {
        return dataEmitter.observeOn(AndroidSchedulers.mainThread())
    }

    fun listenToErrors(): Observable<String> {
        return errorEmitter.observeOn(AndroidSchedulers.mainThread())
    }

    @SuppressLint("CheckResult")
    private fun saveInDb(data: List<Team>) {
        Completable.fromAction {
            try {
                dbHelper.insertTeams(data)
            } catch (ex: Exception) {
                dbHelper.updateTeams(data)
            }
        }
                .compose(RxHelper.asyncToUiCompletable())
                .subscribe(
                        emptyAction("Saving data list is done"),
                        rxError("Error while saving the content in the device")
                )
    }

    private fun loadFromDB(): Observable<List<Team>> {
        return Observable.fromCallable {dbHelper.getAllTeams()}.compose(RxHelper.asyncToUiObservable())
    }

    private fun requestData(): Observable<List<Team>> {
        return requestHelper.request(baseURL)
                .map { response ->
                    val listType = object : TypeToken<List<Team>>() {}.type
                    Gson().fromJson<List<Team>>(response, listType)
                }
                .doOnNext { saveInDb(it) }
    }

    fun stop() {
        RxHelper.unsubscribe(requestDisposable.get())
    }

    fun sort(sortType: SortType) {
        Observable.fromCallable {
            val sortComparator = when (sortType) {
                SortType.Alphabetical -> Alphabetical_Sort
                SortType.Wins -> Wins_Sort
                else -> Losses_Sort
            }
            Collections.sort(currentData, sortComparator)
            dataEmitter.onNext(currentData)
        }
                .compose(RxHelper.asyncToUiObservable())
                .subscribe()

    }

    companion object {
        private const val baseURL = "https://raw.githubusercontent.com/scoremedia/nba-team-viewer/master/input.json"

        private val Alphabetical_Sort = object : Comparator<Team> {
            override fun compare(first: Team, second: Team): Int {
                return first.full_name.compareTo(second.full_name, ignoreCase = true)
            }
        }
        private val Wins_Sort = object : Comparator<Team> {
            override fun compare(first: Team, second: Team): Int {
                return second.wins.compareTo(first.wins)
            }
        }

        private val Losses_Sort = object : Comparator<Team> {
            override fun compare(first: Team, second: Team): Int {
                return second.losses.compareTo(first.losses)
            }
        }
    }
}