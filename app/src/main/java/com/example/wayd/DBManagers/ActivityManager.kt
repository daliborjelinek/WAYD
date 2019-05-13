package com.example.wayd.dbmanagers

import com.example.wayd.dbentities.Activity
import com.example.wayd.enums.Type
import io.realm.Realm
import io.realm.RealmResults

interface ActivityManager {
    fun addOrUpdateActivity(activity: Activity)
    fun getActivity(activity: Activity) : Activity
    fun getActivity(activityId: Long): Activity?
    fun deleteActivity(activity: Activity)
    fun getallActivities() : RealmResults<Activity>
    fun getAllActivitiesByType(type: Type) : RealmResults<Activity>
    fun getRunningActivities() : RealmResults<Activity>
    fun deleteAllActivities()
}