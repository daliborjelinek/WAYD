package com.example.wayd.dbmanagers

import com.example.wayd.dbentities.Record
import io.realm.Realm
import io.realm.RealmResults

import java.time.Duration

interface RecordManager{
    fun getTimeSpent(record: Record) : Long
    fun addOrUpdateRecord(record: Record)
    fun getRecord(id: Long) : Record
    fun deleteRecord(record: Record)
    fun getAllRecords(): RealmResults<Record>
    fun deleteAllRecords()
    fun formatDate(time:Long):String
}