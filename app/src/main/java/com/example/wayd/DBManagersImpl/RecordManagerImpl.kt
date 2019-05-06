package com.example.wayd.dbmanagersImpl

import android.util.Log
import com.example.wayd.dbentities.Record
import com.example.wayd.dbmanagers.RecordManager
import io.realm.Realm
import io.realm.exceptions.RealmException
import java.lang.Exception


class RecordManagerImpl : RecordManager {
    companion object{
        val TAG = "RecordManager"
    }
    override fun getTimeSpent(realm: Realm, record: Record): Long {
        val recordFromDb = getRecord(realm, record._ID)
        val startTime = java.text.DateFormat.getDateTimeInstance().parse(recordFromDb.timeStarted )
        Log.d(TAG, "Start time parse sucesfull")
        val endTime = java.text.DateFormat.getDateTimeInstance().parse(recordFromDb.endTime)
        Log.d(TAG, "End time parse sucesfull")
        return endTime.time.minus(startTime.time)
    }

    override fun addOrUpdateRecord(realm: Realm, record: Record){
        try{
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(record)
            realm.commitTransaction()
            Log.d(TAG, "Succesfullt added/updated record $record")
        } catch (e: RealmException){
            Log.e(TAG, "Error adding/updating record $record")
        }
    }

    override fun getRecord(realm: Realm, id:Long): Record {
        val record = realm.where(Record::class.java).equalTo("_ID", id).findFirst()!!
        Log.d(TAG, "Succesfully retrieved record $record")
        return record
    }

    override fun deleteRecord(realm: Realm, record: Record){
        try{
            realm.beginTransaction()
            record.deleteFromRealm()
            realm.commitTransaction()
            Log.d(TAG, "Succesfullt deleted record $record")
        } catch (e: Exception){
            Log.e(TAG, "deleting record $record")
        }
    }

    fun getNextPrimaryKey(realm: Realm): Long {
        var number: Number? = realm.where(Record::class.java).max("_ID")
        var nextkey: Long = 1
        if(number != null) {
            nextkey = number!!.toLong() + 1
        }
        Log.d(TAG, "Next primary record key generated $nextkey")
        return  nextkey
    }
}