package com.example.wayd.dbmanagers

import com.example.wayd.dbentities.Activity
import com.example.wayd.enums.Type
import io.realm.Realm
import io.realm.RealmResults

interface ActivityManager {
    fun addOrUpdateActivity(realm: Realm, activity: Activity)
    fun getActivity(realm: Realm, activity: Activity) : Activity
    fun deleteActivity(realm: Realm, activity: Activity)
    fun getallActivities(realm: Realm) : RealmResults<Activity>
    fun getAllActivitiesByType(realm: Realm, type: Type) : RealmResults<Activity>
    fun getRunningActivities(realm: Realm) : RealmResults<Activity>
}