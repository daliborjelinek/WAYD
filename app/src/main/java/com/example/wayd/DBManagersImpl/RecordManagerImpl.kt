package com.example.wayd.dbmanagersImpl

import android.util.Log
import com.example.wayd.dbentities.Activity
import com.example.wayd.dbentities.Record
import com.example.wayd.dbmanagers.RecordManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.exceptions.RealmException
import java.lang.Exception
import java.security.acl.LastOwnerException
import java.text.SimpleDateFormat


class RecordManagerImpl(val realm: Realm) : RecordManager {
    companion object{
        val TAG = "RecordManager"
    }
    override fun getTimeSpent(record: Record): Long {
        val endTime:Long
        if (record.endTime != 0L) {
            endTime = record.endTime
        } else {
            endTime = System.currentTimeMillis()
        }
        return endTime  - record.timeStarted
    }

    override fun addOrUpdateRecord(record: Record){
        try{
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(record)
            realm.commitTransaction()
            Log.d(TAG, "Succesfully added/updated record $record")
        } catch (e: RealmException){
            Log.e(TAG, "Error adding/updating record $record")
        }
    }

    override fun getRecord(id:Long): Record {
        Log.d("Testing", id.toString())
        val record = realm.where(Record::class.java).equalTo("_ID", id).findFirst()!!
        Log.d(TAG, "Succesfully retrieved record $record")
            return record
    }

    override fun deleteRecord(record: Record){
        try{
            realm.beginTransaction()
            record.deleteFromRealm()
            realm.commitTransaction()
            Log.d(TAG, "Succesfullt deleted record $record")
        } catch (e: Exception){
            Log.e(TAG, "deleting record $record")
        }
    }

    override fun deleteRecord(Id: Long) {
            val record = getRecord(Id)
        try{
            realm.beginTransaction()
            record.deleteFromRealm()
            realm.commitTransaction()
            Log.d(TAG, "Succesfullt deleted record $record")
        } catch (e: Exception){
            Log.e(TAG, "deleting record $record")
        }
    }

    override fun deleteAllRecords(){
        getAllRecords().iterator().forEach { deleteRecord(it) }
    }

    override fun getAllRecords():RealmResults<Record>{
        val records = realm.where(Record::class.java).findAll()
        Log.d(TAG, "Sucesfully deleted all records $records")
        return records
    }

    override fun formatDate(time:Long):String{
        var format:SimpleDateFormat
        when(time/1000){
            in 0..59 -> format = SimpleDateFormat("ss");
            in 60..3599 -> format = SimpleDateFormat("mm:ss")
            else -> format = SimpleDateFormat("HH:mm:ss")
        }
        return format.format(time)
    }

    fun getNextPrimaryKey(): Long {
        var number: Number? = realm.where(Record::class.java).max("_ID")
        var nextkey: Long = 1
        if(number != null) {
            nextkey = number!!.toLong() + 1
        }
        Log.d(TAG, "Next primary record key generated $nextkey")
        return  nextkey
    }
}