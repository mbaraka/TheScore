package com.thescore.home

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.thescore.database.DBHelper
import com.thescore.model.Team
import com.thescore.rules.SchedulersOverrideRule
import com.thescore.utils.RequestHelper
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Matchers.any
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import java.util.concurrent.TimeUnit


class HomePresenterTest {

    private val scheduler = TestScheduler()
    @Rule @JvmField val schedulersOverride = SchedulersOverrideRule(scheduler)
    @Rule @JvmField val mockitoRule = MockitoJUnit.rule()!!
    @Mock private lateinit var requestHelper: RequestHelper
    @Mock private lateinit var dbHelper: DBHelper

    private lateinit var presenter: HomePresenter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        dbHelper.testMode = true
        presenter = HomePresenter(requestHelper, dbHelper)
    }


    @Test
    fun `when user load data, confirm that client request the data from server`() {
        whenever(requestHelper.request(any())).thenReturn(Observable.just(JSON_RESPONSE))

        presenter.loadData()
        verify(requestHelper).request(any())
    }

    @Test
    fun `when user load data from database, while being offline, only one data is reported`() {
        val exception = NullPointerException("error")
        whenever(requestHelper.request(any())).thenReturn(Observable.error(exception))

        val testObserver = presenter.listenToErrors()
            .subscribeOn(scheduler)
            .test()

        presenter.loadData()

        scheduler.advanceTimeBy(1000, TimeUnit.MILLISECONDS)
        testObserver.assertValueCount(1)
    }


    companion object {

        private const val JSON_RESPONSE = "[\n" +
                "  {\n" +
                "    \"wins\": 45,\n" +
                "    \"losses\": 20,\n" +
                "    \"full_name\": \"Boston Celtics\",\n" +
                "    \"id\": 1,\n" +
                "    \"players\": [\n" +
                "      {\n" +
                "        \"id\": 37729,\n" +
                "        \"first_name\": \"Kadeem\",\n" +
                "        \"last_name\": \"Allen\",\n" +
                "        \"position\": \"SG\",\n" +
                "        \"number\": 45\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]"
    }

}