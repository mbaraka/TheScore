package com.thescore.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T>(private val viewHolderClicked: ViewHolderClicked<T>?) : RecyclerView.Adapter<MyViewHolder>() {

    protected val dataList: ArrayList<T> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val parentView = LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false)
        return MyViewHolder(parentView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        fillViewData(holder, position)

        viewHolderClicked?.let {
            holder.view.setOnClickListener {
                viewHolderClicked.onItemViewClicked(holder.view, dataList[position])
            }
        }

    }

    abstract fun fillViewData(holder: MyViewHolder, position: Int)
    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun getItemCount() = dataList.size

    fun setData(dataList: List<T>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)