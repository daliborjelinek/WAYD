package com.example.WAYD.DBManagersImpl

import android.util.Log
import com.example.WAYD.DBEntities.Activity
import com.example.WAYD.DBManagers.ActivityManager
import com.example.WAYD.Enums.Type
import io.realm.Realm
import io.realm.RealmResults
import io.realm.exceptions.RealmException
import java.lang.Exception

class ActivityManagerImpl : ActivityManager {
    companion object {
        val TAG = "activityManager"
    }
    override fun addOrUpdateActivity(realm: Realm, activity: Activity){
        try{
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(activity)
            realm.commitTransaction()
            Log.d(TAG, "Succesfully created activity $activity")
        } catch (e: RealmException){
            Log.e(TAG, "Error creating activity $activity")
        }
    }

    override fun getActivity(realm: Realm, activity: Activity): Activity {
        val activies = realm.where(Activity::class.java).equalTo("_ID", activity._ID).findFirst()!!
        Log.d(TAG, "Succesfully fetched all  activities")
        return activies
    }

    override fun deleteActivity(realm: Realm, activity: Activity){
        try{
            realm.beginTransaction()
            activity.deleteFromRealm()
            realm.commitTransaction()
            Log.d(TAG, "Succesfully created activity $activity")
        } catch (e: RealmException){
            Log.e(TAG, "Error deleting activity $activity")
        }
    }

    override fun getallActivities(realm: Realm): RealmResults<Activity> {
        return realm.where(Activity::class.java).findAll()
    }

    override fun getAllActivitiesByType(realm: Realm, type: Type): RealmResults<Activity> {
        val activities = realm.where(Activity::class.java).equalTo("type", type.toString()).findAll()
        Log.d(TAG, "Sucesfully fetched activities by type")
        return activities
    }

    override fun getRunningActivities(realm: Realm): RealmResults<Activity> {
        val activities = realm.where(Activity::class.java).equalTo("running", true).findAll()
        Log.d(TAG, "Succesfully fetched all running activities")
        return activities
    }

    fun getNextPrimaryKey(realm: Realm): Long {
        var number: Number? = realm.where(Activity::class.java).max("_ID")
        var nextkey: Long = 1
        if(number != null) {
            nextkey = number!!.toLong() + 1
        }
        Log.d(TAG, "Sucesfully generated next primary key")
        return  nextkey
    }
}