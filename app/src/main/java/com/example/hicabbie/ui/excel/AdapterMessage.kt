package com.example.hicabbie.ui.excel

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.hicabbie.R
import com.example.hicabbie.data.db.entity.EMessage
import com.example.hicabbie.utils.Logger
import com.example.hicabbie.utils.inflateView
import kotlinx.android.synthetic.main.single_msg_mine.view.*
import kotlinx.android.synthetic.main.single_msg_other.view.*

class AdapterMessage : RecyclerView.Adapter<AdapterMessage.MyViewHolder>() {

    var list = mutableListOf<EMessage>()


    fun addToList(mutableList: MutableList<EMessage>) {
        list.addAll(mutableList)
        notifyDataSetChanged()
    }

    private val V_MINE = 22
    private val V_OTHER = 44

    override fun getItemViewType(position: Int) = if (list[position].mine) V_MINE else V_OTHER

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): MyViewHolder {
        return if (type == V_MINE)
            MyViewHolder(parent.inflateView(R.layout.single_msg_mine), type)
        else
            MyViewHolder(parent.inflateView(R.layout.single_msg_other), type)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) = p0.bind()


    inner class MyViewHolder(itemView: View, private val type: Int) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            val single = list[adapterPosition]
            if (type == V_MINE) {
                itemView.textMsg.text = single.msg
            } else {
                itemView.tvName.text = single.name
                itemView.textMsgOther.text = single.msg
            }
        }
    }
}

