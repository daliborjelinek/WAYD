package com.example.wayd.ui

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import com.example.wayd.R
import com.example.wayd.dbentities.Activity
import com.example.wayd.dbentities.Record
import com.example.wayd.dbmanagersImpl.ActivityManagerImpl
import com.example.wayd.dbmanagersImpl.RecordManagerImpl
import com.example.wayd.dbmanagersImpl.WAYDManagerImpl
import io.realm.Realm
import kotlinx.android.synthetic.main.layout_listitem.view.*
import java.text.SimpleDateFormat
import java.util.ArrayList


class RecyclerViewAdapterActivity(val items: ArrayList<Activity>, val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerViewAdapterActivity.ViewHolderActivity>() {

    var onItemClick: ((Activity) -> Unit)? = null
    private val activityManager = ActivityManagerImpl(Realm.getDefaultInstance())
    private val recordManager = RecordManagerImpl(Realm.getDefaultInstance())
    private val WAYDManager = WAYDManagerImpl(Realm.getDefaultInstance())

    override fun onBindViewHolder(holder: ViewHolderActivity, position: Int) {

        holder?.name?.text = items[position].name
        //holder?.time?.text = items[position].time
        holder?.value?.text = items[position].value.toString()

        holder?.iconTextView?.text = items[position].icon
        holder?.cardview?.setCardBackgroundColor(Color.parseColor("#ffffff"))
        DrawableCompat.setTint(holder?.iconTextView.getBackground(), Color.parseColor(items[position].color))
        holder?.iconTextView.setTextColor(Color.parseColor("#ffffff"))


        holder.time.text = WAYDManager.totalTimeSpentWithActivity(items[position]).toString()
        Log.d("TAG", items[position].running.toString() + items[position].name)
        if (items[position].running){
            holder?.cardview?.setCardBackgroundColor(Color.parseColor(items[position].color))
            DrawableCompat.setTint(holder?.iconTextView.getBackground(), Color.parseColor("#ffffff"))
            holder?.iconTextView.setTextColor(Color.parseColor(items[position].color))

        }
        holder?.iconTextView.setOnClickListener {
            val activity = Activity(
                items[position]._ID,
                items[position].name,
                items[position].color,
                items[position].value,
                items[position].running,
                items[position].type,
                items[position].icon
                )
            if(items[position].running){
                val dBRecord = WAYDManager.getAllRecordToActivity(items[position]._ID)?.last()
                if (dBRecord != null) {
                    val lastRecord = Record(dBRecord._ID, dBRecord.timeStarted, dBRecord.activityId, dBRecord.endTime)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    lastRecord?.endTime = System.currentTimeMillis()
                    recordManager.addOrUpdateRecord(lastRecord)
                    activity.running = false
                    holder?.cardview?.setCardBackgroundColor(Color.parseColor("#ffffff"))
                    DrawableCompat.setTint(holder?.iconTextView.getBackground(), Color.parseColor(items[position].color))
                    holder?.iconTextView.setTextColor(Color.parseColor("#ffffff"))

                }

            } else{
                val newRecord = Record(
                    recordManager.getNextPrimaryKey(),
                    System.currentTimeMillis(),
                    items[position]._ID,
                    0L)
                recordManager.addOrUpdateRecord(newRecord)
                activity.running = true
                holder?.cardview?.setCardBackgroundColor(Color.parseColor(items[position].color))
                DrawableCompat.setTint(holder?.iconTextView.getBackground(), Color.parseColor("#ffffff"))
                holder?.iconTextView.setTextColor(Color.parseColor(items[position].color))
            }
            activityManager.addOrUpdateActivity(activity)
        }

    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderActivity {
        return ViewHolderActivity(LayoutInflater.from(context).inflate(R.layout.layout_listitem, parent, false))
    }


    inner class ViewHolderActivity(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {


        val name = v.nameTextView
        val time = v.timeTextView
        val value = v.valueTextView
        val cardview = v.listItemCardView
        val iconTextView = v.activityIconView
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }

    }
}