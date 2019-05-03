package com.example.WAYD.DBEntities

import io.realm.RealmObject
import java.sql.Time

open class Record(
    open var _ID : Long = 0,
    open var timeStarted:String = "",
    open var activityId: Long = 0,
    open var endTime:String= ""
): RealmObject()