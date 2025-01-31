package com.example.WAYD.DBManagers

import com.example.WAYD.DBEntities.Activity
import com.example.WAYD.DBEntities.Record
import io.realm.Realm
import io.realm.RealmResults
import java.sql.Time

interface WAYDManager{
    fun totalTimeSpentWithActivity(realm: Realm, activity: Activity):Long
    fun averageTimeSpentWithActivity(realm: Realm, activity: Activity):Long
    fun getAllRecordToActivity(realm: Realm, activity: Activity):RealmResults<Record>
    fun timeSpentWithActivityDuringTimePeriod(realm: Realm, activity: Activity, timeperiod: Time):Long
    fun valueOfActivity(realm: Realm, activity: Activity):Number
    fun valueOfActivityOverTimePeriod(realm: Realm, activity: Activity, timeperiod: Time):Number
}