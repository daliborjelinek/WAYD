package com.example.WAYD.DBEntities

import com.example.WAYD.Enums.Color
import com.example.WAYD.Enums.Type
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Activity(
    @PrimaryKey open var _ID:Long = 0,
    open var name:String = "",
    open var color: String = "Blue",
    open var value: Double = Double.MIN_VALUE,
    open var running: Boolean = false,
    open var type: String = ""
): RealmObject()