package com.example.wayd.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wayd.R
import com.example.wayd.dbentities.Activity
import com.example.wayd.dbentities.Record
import com.example.wayd.dbmanagers.WAYDManager
import com.example.wayd.dbmanagersImpl.ActivityManagerImpl
import com.example.wayd.dbmanagersImpl.RecordManagerImpl
import com.example.wayd.dbmanagersImpl.WAYDManagerImpl
import io.realm.Realm
import kotlinx.android.synthetic.main.recycler_view_item_record.view.*
import java.text.SimpleDateFormat
import java.util.ArrayList

class RecyclerViewAdapterRecord(val items: ArrayList<Record>, val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerViewAdapterRecord.ViewHolderRecord>() {

    var onItemClick: ((Record) -> Unit)? = null
    val recordManager = RecordManagerImpl(Realm.getDefaultInstance())
    val WAYDManager = WAYDManagerImpl(Realm.getDefaultInstance())
    val activityManager = ActivityManagerImpl(Realm.getDefaultInstance())
    override fun onBindViewHolder(holder: ViewHolderRecord, position: Int) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
      //  holder?.timeStarted?.text = dateFormat.format(items[position].timeStarted)
      //  holder?.endTime?.text = dateFormat.format(items[position].endTime)
      //  holder.duration.text = recordManager.formatDate(recordManager.getTimeSpent(items[position]))
      //  holder.value.text = WAYDManager.valueOfRecord(activityManager.getActivity(items[position].activityId)!!,items[position]).toString()

    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRecord {
        return ViewHolderRecord(LayoutInflater.from(context).inflate(R.layout.recycler_view_item_record, parent, false))
    }


    inner class ViewHolderRecord(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {

       // val timeStarted = v.recordStartTimeTextView
       // val endTime = v.recordEndTimeTextView
       // val value = v.recordValueTextView
       // val duration = v.recordDurationTextView

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }
    }
}