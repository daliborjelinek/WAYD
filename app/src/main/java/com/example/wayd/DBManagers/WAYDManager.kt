package com.example.wayd.dbmanagers

import com.example.wayd.dbentities.Activity
import com.example.wayd.dbentities.Record
import io.realm.Realm
import io.realm.RealmResults
import java.sql.Time

interface WAYDManager{
    fun totalTimeSpentWithActivity( activity: Activity):Long
    fun averageTimeSpentWithActivity( activity: Activity):Long
    fun getAllRecordToActivity( activity: Activity):RealmResults<Record>
    fun getAllRecordToActivity( activityId: Long):RealmResults<Record>?
    fun valueOfActivity( activity: Activity):Number
    fun valueOfRecord(activity: Activity, record: Record):Double
    fun safeDeleteActivity(activity: Activity)
}