package com.example.astrodashalib.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.astrodashalib.R
import com.example.astrodashalib.data.models.ChatModel
import com.example.astrodashalib.gone
import com.example.astrodashalib.inflate
import com.example.astrodashalib.localDB.DbConstants
import com.example.astrodashalib.visible
import kotlinx.android.synthetic.main.chat_list_item.view.*


/**
 * Created by himanshu on 29/09/17.
 */
class ChatAdapter(val context: Context, var onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    var chatModelArrayList: ArrayList<ChatModel> = ArrayList()


    fun setData(chatlist: ArrayList<ChatModel>) {
        this.chatModelArrayList = chatlist
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        try {
            val chat = chatModelArrayList[position]
            holder.bind(chat)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder = ChatViewHolder(parent.inflate(R.layout.chat_list_item), onItemClickListener)

    override fun getItemCount(): Int = chatModelArrayList.size

    class ChatViewHolder(view: View, var onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(view) {

        fun bind(chat: ChatModel) = with(itemView) {
            if (chat.date != null && chat.date.trim().isNotEmpty() && chat.localId == null) {
                you.gone()
                me.gone()
                date_tv.visible()
                date_tv.setText(chat.date)
                chat_status_iv.gone()
            } else if (!chat.isIncoming) {
                you.gone()
                date_tv.gone()
                me.visible()
                meNameTv.visible()
                meNameTv.text = "sent by you"
                youNameTv.gone()
                chat_me.setText(chat.value)
                when (chat.chatStatus) {
                    DbConstants.STATUS_NOT_PAID -> {
                        chat_status_iv.gone()
                        chat_status_tv.visible()
                    }
                    DbConstants.STATUS_SENDING -> {
                        chat_status_iv.setImageResource(R.drawable.schedule_button)
                        chat_status_iv.visible()
                        chat_status_tv.gone()
                    }
                    DbConstants.STATUS_READ -> {
                        chat_status_iv.setImageResource(R.drawable.double_tick)
                        chat_status_iv.visible()
                        chat_status_tv.gone()
                    }
                    else -> {
                        chat_status_iv.setImageResource(R.drawable.single_tick)
                        chat_status_iv.visible()
                        chat_status_tv.gone()
                    }
                }
            } else {
                you.visible()
                date_tv.gone()
                meNameTv.gone()
                youNameTv.visible()
                youNameTv.text = if (chat.fromUserName.equals("system")) "sent by system" else "sent by disciple"
                me.gone()
                chat_you.text = chat.value
                chat_status_iv.gone()
            }

            setOnClickListener { v -> onItemClickListener.onItemClicked(v, getAdapterPosition()); }
        }

    }

    interface OnItemClickListener {
        fun onItemClicked(view: View, position: Int)
    }
}