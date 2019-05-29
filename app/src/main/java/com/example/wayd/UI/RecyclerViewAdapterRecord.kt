package com.example.wayd.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wayd.R
import com.example.wayd.Utils.Constants
import com.example.wayd.Utils.Constants.Companion.HOUR_IN_MILIS
import com.example.wayd.dbentities.Activity
import com.example.wayd.dbentities.Record
import com.example.wayd.dbmanagers.WAYDManager
import com.example.wayd.dbmanagersImpl.ActivityManagerImpl
import com.example.wayd.dbmanagersImpl.RecordManagerImpl
import com.example.wayd.dbmanagersImpl.WAYDManagerImpl
import io.realm.Realm
import kotlinx.android.synthetic.main.layout_listitem.view.*
import kotlinx.android.synthetic.main.recycler_view_item_record.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.ArrayList

class RecyclerViewAdapterRecord(val items: ArrayList<Record>, val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerViewAdapterRecord.ViewHolderRecord>() {

    var onItemClick: ((Record) -> Unit)? = null
    val recordManager = RecordManagerImpl(Realm.getDefaultInstance())
    val WAYDManager = WAYDManagerImpl(Realm.getDefaultInstance())
    val activityManager = ActivityManagerImpl(Realm.getDefaultInstance())
    override fun onBindViewHolder(holder: ViewHolderRecord, position: Int) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd");
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        holder?.startDate?.text = dateFormat.format(items[position].timeStarted)
        holder?.timeStarted?.text = timeFormat.format(items[position].timeStarted)
        holder?.endTime?.text = timeFormat.format(items[position].endTime)
        holder?.endDate?.text = dateFormat.format(items[position].endTime)
        holder?.duration.text = timeFormat.format(recordManager.getTimeSpent(items[position]) - HOUR_IN_MILIS)

    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRecord {
        return ViewHolderRecord(LayoutInflater.from(context).inflate(R.layout.recycler_view_item_record, parent, false))
    }


    inner class ViewHolderRecord(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
        val timeStarted = v.startTimeTv
        val endTime = v.endTimeTv
        val duration = v.durationTv
        val endDate = v.endDataTv
        val startDate = v.startDateTv
        val totalValue = v.totalValueTv
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }
    }
}