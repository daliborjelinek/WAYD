package com.example.wayd.dbentities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.sql.Time

open class Record(
    @PrimaryKey open var _ID : Long = 0,
    open var timeStarted:Long = 0L,
    open var activityId: Long = 0,
    open var endTime:Long = 0L
): RealmObject()