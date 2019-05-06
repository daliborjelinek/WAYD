package com.example.wayd.dbmanagers

import com.example.wayd.dbentities.Record
import io.realm.Realm

import java.time.Duration

interface RecordManager{
    fun getTimeSpent(realm:Realm, record: Record) : Long
    fun addOrUpdateRecord(realm: Realm, record: Record)
    fun getRecord(realm: Realm, id: Long) : Record
    fun deleteRecord(realm: Realm, record: Record)
    
}