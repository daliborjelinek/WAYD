package com.example.wayd.ui

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import com.example.wayd.R
import com.example.wayd.Utils.Constants.Companion.HOUR_IN_MILIS
import com.example.wayd.dbentities.Activity
import com.example.wayd.dbentities.Record
import com.example.wayd.dbmanagersImpl.ActivityManagerImpl
import com.example.wayd.dbmanagersImpl.RecordManagerImpl
import com.example.wayd.dbmanagersImpl.WAYDManagerImpl
import com.example.wayd.enums.Type
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
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        holder?.name?.text = items[position].name
        holder?.value?.amount = items[position].value.toFloat()
        holder?.iconTextView?.text = items[position].icon
        holder?.cardview?.setCardBackgroundColor(Color.parseColor("#ffffff"))
        DrawableCompat.setTint(holder?.iconTextView.getBackground(), Color.parseColor(items[position].color))
        holder?.iconTextView.setTextColor(Color.parseColor("#ffffff"))
        holder?.totalTimeSpent.text = timeFormat.format(WAYDManager.totalTimeSpentWithActivity(items[position])-HOUR_IN_MILIS)
        //holder?.totalValue.amount = (WAYDManager.totalTimeSpentWithActivity(items[position])/1000/60/60*items[position].value).toFloat()
        holder?.totalValue.amount = WAYDManager.valueOfActivity(items[position]).toFloat()

        //holder.time.text = WAYDManager.totalTimeSpentWithActivity(items[position]).toString()
        Log.d("TAG", items[position].running.toString() + items[position].name)
        if (items[position].running){
            holder?.cardview?.setCardBackgroundColor(Color.parseColor(items[position].color))
            DrawableCompat.setTint(holder?.iconTextView.getBackground(), Color.parseColor("#ffffff"))
            holder?.iconTextView.setTextColor(Color.parseColor(items[position].color))

        }

        if(items[position].type == Type.perHour.name){
            holder?.activityTypeTextView.text = context.getString(R.string.per_hour)
        }
        else holder?.activityTypeTextView.text = context.getString(R.string.per_activity)

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
                    lastRecord?.endTime = System.currentTimeMillis()
                    recordManager.addOrUpdateRecord(lastRecord)
                    activity.running = false
                    holder?.cardview?.setCardBackgroundColor(Color.parseColor("#ffffff"))
                    DrawableCompat.setTint(holder?.iconTextView.getBackground(), Color.parseColor(items[position].color))
                    holder?.iconTextView.setTextColor(Color.parseColor("#ffffff"))
                    holder?.totalTimeSpent.text = timeFormat.format(WAYDManager.totalTimeSpentWithActivity(items[position])-HOUR_IN_MILIS)
                    //holder?.totalValue.amount = (WAYDManager.totalTimeSpentWithActivity(items[position])/1000/60/60*items[position].value).toFloat()
                    holder?.totalValue.amount = WAYDManager.valueOfActivity(items[position]).toFloat()
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

        val totalTimeSpent = v.totalTimeSpenTv
        val value = v.valueTextView
        val totalValue = v.totalValueTv
        val name = v.nameTextView
        val activityTypeTextView = v.activityTypeTextView
        //val time = v.timeTextView
       // val value = v.valueTextView
        val cardview = v.listItemCardView
        val iconTextView = v.activityIconView
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }

    }
}