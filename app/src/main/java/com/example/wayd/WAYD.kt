package com.example.wayd

import android.app.Application
import android.util.Log
import com.example.wayd.dbentities.Record
import io.realm.Realm
import io.realm.RealmConfiguration








class WAYD : Application(){
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

    }


    override fun onTerminate() {
        super.onTerminate()
    }


}