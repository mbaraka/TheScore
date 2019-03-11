package com.thescore.home

import android.widget.TextView
import com.thescore.helpers.BaseAdapter
import com.thescore.helpers.MyViewHolder
import com.thescore.R
import com.thescore.helpers.ViewHolderClicked
import com.thescore.model.Team

class TeamAdapter(viewHolderClicked: ViewHolderClicked<Team>) : BaseAdapter<Team>(viewHolderClicked) {

    override fun fillViewData(holder: MyViewHolder, position: Int) {
        val context = holder.view.context

        holder.view.findViewById<TextView>(R.id.fullName)?.text = context.getString(R.string.teamName, dataList[position].full_name)
        holder.view.findViewById<TextView>(R.id.wins)?.text = context.getString(R.string.wins, dataList[position].wins)
        holder.view.findViewById<TextView>(R.id.losses)?.text = context.getString(R.string.losses, dataList[position].losses)
    }

    override fun getLayoutId(): Int {
        return R.layout.team_item_view
    }
}