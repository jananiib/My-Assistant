package com.bot.alvinbot.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bot.alvinbot.R
import com.bot.alvinbot.data.model.ChatMessage


class ChatMessageAdapter(
    context: Context?,
    var data: List<ChatMessage?>?
) :
    ArrayAdapter<ChatMessage?>(context!!, R.layout.item_mine_message, data!!) {
    override fun getViewTypeCount(): Int {
        // my message, other message, my image, other image
        return 4
    }

    override fun getItemViewType(position: Int): Int {
        val item: ChatMessage? = getItem(position)
        return if (item?.isMine!! && !item.isImage) MY_MESSAGE else if (!item.isMine && !item.isImage) OTHER_MESSAGE else if (item.isMine && item.isImage) MY_IMAGE else OTHER_IMAGE
    }

    fun getListData(): List<ChatMessage?>? {
        return data
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val viewType = getItemViewType(position)
        if (viewType == MY_MESSAGE) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.item_mine_message, parent, false)
            val textView =
                convertView!!.findViewById<View>(R.id.text) as TextView
            textView.setText(getItem(position)?.content)
        } else if (viewType == OTHER_MESSAGE) {
            convertView = LayoutInflater.from(context)
                .inflate(R.layout.item_other_message, parent, false)
            val textView =
                convertView!!.findViewById<View>(R.id.text) as TextView
            textView.setText(getItem(position)?.content)
        } else if (viewType == MY_IMAGE) {
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_image, parent, false);
        } else {
            // convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_other_image, parent, false);
        }
//        convertView!!.findViewById<View>(R.id.chatMessageView)
//            .setOnClickListener { }
//        return convertView
        return convertView!!
    }

    companion object {
        private const val MY_MESSAGE = 0
        private const val OTHER_MESSAGE = 1
        private const val MY_IMAGE = 2
        private const val OTHER_IMAGE = 3
    }
}
