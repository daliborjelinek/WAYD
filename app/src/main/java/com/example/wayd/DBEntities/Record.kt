package com.example.wayd.dbentities

import io.realm.RealmObject
import java.sql.Time

open class Record(
    open var _ID : Long = 0,
    open var timeStarted:String = "",
    open var activityId: Long = 0,
    open var endTime:String= ""
): RealmObject()