package com.thescore.home

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.thescore.database.DBHelper
import com.thescore.model.Player
import com.thescore.model.Team
import com.thescore.rules.SchedulersOverrideRule
import com.thescore.utils.RequestHelper
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Matchers.any
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import java.util.concurrent.Callable
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
    fun `when user load data, confirm that 2 data being sent`() {
        whenever(requestHelper.request(Matchers.any())).thenReturn(Observable.just(JSON_RESPONSE))
        whenever(dbHelper.getAllTeams()).thenReturn(list)

        val testObserver = presenter.listenToData()
            .subscribeOn(scheduler)
            .test()

        presenter.loadData()

        verify(requestHelper).request(Matchers.any())

        scheduler.advanceTimeBy(2000, TimeUnit.MILLISECONDS)

        testObserver
            .assertValueCount(2)
            .assertValues(list, list)

        scheduler.advanceTimeBy(1000, TimeUnit.MILLISECONDS)
        verify(dbHelper).insertTeams(Matchers.anyList())
    }

    @Test
    fun `when user load data and the request fails, only database one being sent`() {
        whenever(requestHelper.request(any())).thenReturn(error())
        whenever(dbHelper.getAllTeams()).thenReturn(list)

        val errors = presenter.listenToErrors()
            .subscribeOn(scheduler)
            .test()

        val data = presenter.listenToData()
            .subscribeOn(scheduler)
            .test()

        presenter.loadData()

        scheduler.advanceTimeBy(1000, TimeUnit.MILLISECONDS)
        data.assertValueCount(1).assertValues(list)
        errors.assertValueCount(1)
    }

    @Test
    fun `when user load data and the request fails, only server one being sent`() {
        whenever(requestHelper.request(Matchers.any())).thenReturn(Observable.just(JSON_RESPONSE))
        whenever(dbHelper.getAllTeams()).thenReturn(ArrayList())

        val data = presenter.listenToData()
            .subscribeOn(scheduler)
            .test()

        presenter.loadData()

        scheduler.advanceTimeBy(2000, TimeUnit.MILLISECONDS)
        data.assertValues(Matchers.anyList(), list)
    }


    private fun error(): Observable<String> {
        return Observable.error(NullPointerException("Testing exception"))
    }

    companion object {

        private val list = getTeams()

        private fun getTeams(): ArrayList<Team> {
            val players = ArrayList<Player>()
            val player = Player("Kadeem", 37729, "Allen", 45, "SG")
            players.add(player)

            val teams = ArrayList<Team>()
            teams.add(Team("Boston Celtics", 1, 20, players, 45))

            return teams
        }

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