package com.thescore.teamActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.thescore.R
import com.thescore.com.thescore.teamActivity.PlayerRecyclerAdapter
import com.thescore.helpers.BaseRecyclerAdapter
import com.thescore.model.Player
import com.thescore.model.Team
import kotlinx.android.synthetic.main.activity_team.*

class TeamActivity : AppCompatActivity() {

    private lateinit var baseRecyclerAdapter: BaseRecyclerAdapter<Player>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)
        val team: Team = intent.extras?.get(EXTRA_TEAM_KEY) as Team
        title = team.full_name

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider))

        recycler.addItemDecoration(dividerItemDecoration)

        baseRecyclerAdapter = PlayerRecyclerAdapter()
        recycler.adapter = baseRecyclerAdapter

        fillData(team)
    }

    private fun fillData(team: Team) {
        wins.text = getString(R.string.wins, team.wins)
        losses.text = getString(R.string.losses, team.losses)

        baseRecyclerAdapter.setData(team.players)
    }


    companion object {
        const val EXTRA_TEAM_KEY = "team"
    }
}