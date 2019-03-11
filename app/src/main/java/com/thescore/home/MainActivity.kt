package com.thescore.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.thescore.Application
import com.thescore.helpers.BaseAdapter
import com.thescore.R
import com.thescore.helpers.ViewHolderClicked
import com.thescore.model.Team
import com.thescore.utils.rxError
import io.reactivex.disposables.SerialDisposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import com.thescore.com.thescore.model.SortType

class MainActivity : AppCompatActivity(), ViewHolderClicked<Team> {

    private lateinit var baseAdapter: BaseAdapter<Team>
    @Inject lateinit var homePresenter: HomePresenter
    private val dataDisposable = SerialDisposable()
    private val errorDisposable = SerialDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Application.appComponent.inject(this)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider))

        recycler.addItemDecoration(dividerItemDecoration)

        baseAdapter = TeamAdapter(this)
        recycler.adapter = baseAdapter

        listenToEvents()
        homePresenter.loadSaveCities()
    }

    override fun onItemViewClicked(view: View, item: Team) {

    }

    @SuppressLint("CheckResult")
    private fun listenToEvents() {
        dataDisposable.set(homePresenter.listenToCities()
                .subscribe(Consumer {
                    baseAdapter.setData(it)
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
