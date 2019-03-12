package com.thescore.com.thescore.teamActivity

import android.widget.TextView
import com.thescore.R
import com.thescore.helpers.BaseRecyclerAdapter
import com.thescore.helpers.MyViewHolder
import com.thescore.helpers.ViewHolderClicked
import com.thescore.model.Player
import com.thescore.model.Team

class PlayerRecyclerAdapter : BaseRecyclerAdapter<Player>(null) {

    override fun fillViewData(holder: MyViewHolder, position: Int) {
        val context = holder.view.context

        holder.view.findViewById<TextView>(R.id.firstName).text = context.getString(R.string.firstName, dataList[position].first_name)
        holder.view.findViewById<TextView>(R.id.lastName).text = context.getString(R.string.lastName, dataList[position].last_name)
        holder.view.findViewById<TextView>(R.id.position).text = context.getString(R.string.position, dataList[position].position)
        holder.view.findViewById<TextView>(R.id.number).text = context.getString(R.string.number, dataList[position].number)
    }

    override fun getLayoutId(): Int {
        return R.layout.player_item_view
    }
}