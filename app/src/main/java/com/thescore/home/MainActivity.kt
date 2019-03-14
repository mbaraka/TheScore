package com.thescore.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.thescore.Application
import com.thescore.helpers.BaseRecyclerAdapter
import com.thescore.R
import com.thescore.helpers.ViewHolderClicked
import com.thescore.model.Team
import com.thescore.utils.rxError
import io.reactivex.disposables.SerialDisposable
import io.reactivex.functions.Consumer
import javax.inject.Inject
import com.thescore.com.thescore.model.SortType
import com.thescore.teamActivity.TeamActivity
import com.thescore.utils.RxHelper
import kotlinx.android.synthetic.main.activity_team.*

class MainActivity : AppCompatActivity(), ViewHolderClicked<Team> {

    private lateinit var baseRecyclerAdapter: BaseRecyclerAdapter<Team>
    @Inject lateinit var homePresenter: HomePresenter
    private val dataDisposable = SerialDisposable()
    private val errorDisposable = SerialDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Application.appComponent.inject(this)
        title = getString(R.string.home)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider))

        recycler.addItemDecoration(dividerItemDecoration)

        baseRecyclerAdapter = TeamRecyclerAdapter(this)
        recycler.adapter = baseRecyclerAdapter

        listenToEvents()
        homePresenter.loadData()
    }

    override fun onItemViewClicked(view: View, item: Team) {
        val intent = Intent(this, TeamActivity::class.java)
        intent.putExtra(TeamActivity.EXTRA_TEAM_KEY, item)
        startActivity(intent)
    }

    @SuppressLint("CheckResult")
    private fun listenToEvents() {
        dataDisposable.set(homePresenter.listenToData()
                .subscribe(Consumer {
                    baseRecyclerAdapter.setData(it)
                }, rxError("Failed to listen to data changes"))
        )

        errorDisposable.set(homePresenter.listenToErrors()
                .subscribe(Consumer {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }, rxError("Failed to listen to data requests"))
        )
    }

    override fun onStop() {
        homePresenter.stop()
        RxHelper.unsubscribe(dataDisposable.get())
        RxHelper.unsubscribe(errorDisposable.get())
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.menu_alphabetical -> {
                homePresenter.sort(SortType.Alphabetical)
                true
            }
            R.id.menu_wins -> {
                homePresenter.sort(SortType.Wins)
                true
            }
            R.id.menu_losses -> {
                homePresenter.sort(SortType.Losses)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}
