package com.example.wayd.dbmanagersImpl

import android.util.Log
import com.example.wayd.dbentities.Activity
import com.example.wayd.dbmanagers.ActivityManager
import com.example.wayd.enums.Type
import io.realm.Realm
import io.realm.RealmResults
import io.realm.exceptions.RealmException

class ActivityManagerImpl(val realm: Realm) : ActivityManager {
    companion object {
        val TAG = "activityManager"
    }
    override fun addOrUpdateActivity(activity: Activity){
        try{
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(activity)
            realm.commitTransaction()
            Log.d(TAG, "Succesfully created activity $activity")
        } catch (e: RealmException){
            Log.e(TAG, "Error creating activity $activity")
        }
    }

    override fun getActivity(activity: Activity): Activity {
        val activity = realm.where(Activity::class.java).equalTo("_ID", activity._ID).findFirst()!!
        Log.d(TAG, "Succesfully fetched activity $activity")
        return activity
    }

    override fun getActivity(activityId: Long): Activity? {
        if (activityId != 0L){
            val activity:Activity? = realm.where(Activity::class.java).equalTo("_ID", activityId).findFirst()!!
            Log.d(TAG, "Succesfully fetched activity $activity")
            return activity
        }
        Log.d(TAG, "Activity with ID $activityId not found")
        return null
    }

    override fun deleteActivity(activity: Activity){
        try{
            realm.beginTransaction()
            activity.deleteFromRealm()
            realm.commitTransaction()
            Log.d(TAG, "Succesfully deleted activity $activity")
        } catch (e: RealmException){
            Log.e(TAG, "Error deleting activity $activity")
        }
    }

    override fun getallActivities(): RealmResults<Activity> {
        return realm.where(Activity::class.java).findAll()
    }

    override fun getAllActivitiesByType(type: Type): RealmResults<Activity> {
        val activities = realm.where(Activity::class.java).equalTo("type", type.toString()).findAll()
        Log.d(TAG, "Sucesfully fetched activities by type")
        return activities
    }

    override fun getRunningActivities(): RealmResults<Activity> {
        val activities = realm.where(Activity::class.java).equalTo("running", true).findAll()
        Log.d(TAG, "Succesfully fetched all running activities")
        return activities
    }
    override fun deleteAllActivities(){
        getallActivities().iterator().forEach { deleteActivity(it) }
    }

    fun getNextPrimaryKey(): Long {
        var number: Number? = realm.where(Activity::class.java).max("_ID")
        var nextkey: Long = 1
        if(number != null) {
            nextkey = number!!.toLong() + 1
        }
        Log.d(TAG, "Sucesfully generated next primary key")
        return  nextkey
    }


}