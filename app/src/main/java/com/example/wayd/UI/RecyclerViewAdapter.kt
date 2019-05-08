package com.example.wayd.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wayd.R
import com.example.wayd.activities.TmpActivity
import kotlinx.android.synthetic.main.layout_listitem.view.*
import java.util.ArrayList

class RecyclerViewAdapter(val items: ArrayList<TmpActivity>, val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>(){



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder?.name?.text = items[position].name
        holder?.time?.text = items[position].time
        holder?.value?.text = items[position].value
        holder?.carview?.setCardBackgroundColor(Color.parseColor(items[position].color))
      //  holder?.iconTextView?.text = items[position].icon


    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_listitem, parent, false))
    }

}

class ViewHolder(v: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {

    val name = v.nameTextView
    val time = v.timeTextView
    val value = v.valueTextView
    val carview = v.listItemCardView
    val iconTextView = v.iconTextView

}
