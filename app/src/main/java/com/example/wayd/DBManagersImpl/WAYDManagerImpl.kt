package com.example.wayd.dbmanagersImpl

import android.util.Log
import com.example.wayd.dbentities.Activity
import com.example.wayd.dbentities.Record
import com.example.wayd.dbmanagers.WAYDManager
import io.realm.Realm
import io.realm.RealmResults
import java.sql.Time
import java.text.SimpleDateFormat


class WAYDManagerImpl(val realm: Realm) : WAYDManager {
    val recordManager = RecordManagerImpl(Realm.getDefaultInstance())
    val activityManager = ActivityManagerImpl(Realm.getDefaultInstance())
    override fun valueOfRecord(activity: Activity, record: Record):Double {
        return activity.value*toHours(recordManager.getTimeSpent(record))
    }

    companion object {
        val MILIS = 1000
        val SEC = 60
        val MIN = 60
        val HOUR = 24
        val TAG = "asdf"
    }
    val recordManagerImpl = RecordManagerImpl(realm)
    override fun totalTimeSpentWithActivity( activity: Activity): Long {
        val records = getAllRecordToActivity(activity)
        val sum = records.sumBy { rec -> recordManagerImpl.getTimeSpent(rec).toInt()}.toLong()
        Log.d(TAG, "Total time spent $sum with activity $activity")
        return sum
    }

    override fun averageTimeSpentWithActivity( activity: Activity): Long {
        return totalTimeSpentWithActivity(activity) / getAllRecordToActivity(activity).size
    }

    override fun getAllRecordToActivity( activity: Activity): RealmResults<Record> {
        val records = realm.where(Record::class.java).equalTo("activityId", activity._ID).findAll()
        Log.d(TAG, "Sucesfully fetched all records to activity $activity")
        Log.d(TAG, records.size.toString())
        return records
    }
    override fun getAllRecordToActivity( activityId: Long): RealmResults<Record>? {
        val records: RealmResults<Record>? = realm.where(Record::class.java).equalTo("activityId", activityId).findAll()
        Log.d(TAG, "Sucesfully fetched all records to activity with ID $activityId")
        return records
    }



    override fun valueOfActivity( activity: Activity): Double {
        if (activity.type.equals("perHour")){
            val value = totalTimeSpentWithActivity(activity)/1000/60/60 * activity.value
            Log.d(TAG, "Sucesfully calculated perHour Value to $activity")
            return value
        }
        val value = getAllRecordToActivity(activity).size * activity.value
        Log.d(TAG, "Sucesfully calculated perRecord Value to $activity")
        return value
    }


    override fun safeDeleteActivity(activity: Activity) {
        val records = getAllRecordToActivity(activity)
        records.forEach{recordManager.deleteRecord(it)}
        activityManager.deleteActivity(activity)
    }
    fun toHours(timeInMilis:Long):Long{
        return timeInMilis / (MIN * SEC * MILIS*1000) % HOUR
    }

    fun formatMilisToReadable(timeInMilis: Long, dateFormat: String):String{
        val formater = SimpleDateFormat(dateFormat)
        val formated = formater.format(timeInMilis)
        Log.d(TAG, "Sucesfully formated $timeInMilis to $formated")
        return formated
    }

}
