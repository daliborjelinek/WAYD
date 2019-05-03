package com.example.WAYD

import android.app.Application
import io.realm.Realm

class WAYD : Application(){
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }


}