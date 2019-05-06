package com.example.wayd.dbmanagersImpl

import android.util.Log
import com.example.wayd.dbentities.Activity
import com.example.wayd.dbentities.Record
import com.example.wayd.dbmanagers.WAYDManager
import io.realm.Realm
import io.realm.RealmResults
import java.sql.Time
import java.text.SimpleDateFormat


class WAYDManagerImpl : WAYDManager {
    companion object {
        val MILIS = 1000
        val SEC = 60
        val MIN = 60
        val HOUR = 24
        val TAG = "WAYD manager"
    }
    val recordManagerImpl = RecordManagerImpl()
    override fun totalTimeSpentWithActivity(realm: Realm, activity: Activity): Long {
        val records = getAllRecordToActivity(realm, activity)
        val sum = records.sumBy { rec -> recordManagerImpl.getTimeSpent(realm, rec).toInt()}.toLong()
        Log.d(TAG, "Sucesfully summed all recors to activity $activity")
        return sum
    }

    override fun averageTimeSpentWithActivity(realm: Realm, activity: Activity): Long {
        return totalTimeSpentWithActivity(realm, activity) / getAllRecordToActivity(realm, activity).size
    }

    override fun getAllRecordToActivity(realm: Realm, activity: Activity): RealmResults<Record> {
        val records = realm.where(Record::class.java).equalTo("activityId", activity._ID).findAll()
        Log.d(TAG, "Sucesfully fetched all records to activity $activity")
        return records
    }

    override fun timeSpentWithActivityDuringTimePeriod(realm: Realm, activity: Activity, timeperiod: Time): Long{
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun valueOfActivity(realm: Realm, activity: Activity): Double {
        if (activity.type.equals("perHour")){
            val value = toHours(totalTimeSpentWithActivity(realm ,activity)) * activity.value
            Log.d(TAG, "Sucesfully calculated perHour Value to $activity")
            return value
        }
        val value = getAllRecordToActivity(realm, activity).size * activity.value
        Log.d(TAG, "Sucesfully calculated perRecord Value to $activity")
        return value
    }

    override fun valueOfActivityOverTimePeriod(realm: Realm, activity: Activity, timeperiod: Time): Number {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun toHours(timeInMilis:Long):Long{
        return timeInMilis / (MIN * SEC * MILIS) % HOUR
    }

    fun formatMilisToReadable(timeInMilis: Long, dateFormat: String):String{
        val formater = SimpleDateFormat(dateFormat)
        val formated = formater.format(timeInMilis)
        Log.d(TAG, "Sucesfully formated $timeInMilis to $formated")
        return formated
    }

}
