package com.example.WAYD.DBManagers

import com.example.WAYD.DBEntities.Record
import io.realm.Realm

import java.time.Duration

interface RecordManager{
    fun getTimeSpent(realm:Realm, record: Record) : Long
    fun addOrUpdateRecord(realm: Realm, record: Record)
    fun getRecord(realm: Realm, id: Long) : Record
    fun deleteRecord(realm: Realm, record: Record)
    
}