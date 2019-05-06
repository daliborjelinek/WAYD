package com.example.wayd.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.wayd.dbmanagersImpl.WAYDManagerImpl


import com.example.wayd.R
import com.example.wayd.dbentities.Activity
import com.example.wayd.dbmanagersImpl.ActivityManagerImpl
import com.example.wayd.dbmanagersImpl.RecordManagerImpl
import com.example.wayd.enums.Type
import io.realm.Realm
import java.lang.Exception
import java.util.Date

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG: String = "MainActivity"
    }

    private val activityManager = ActivityManagerImpl()
    private val recordManager = RecordManagerImpl()
    private val WAYDManager = WAYDManagerImpl()
    private lateinit var realm: Realm
    private lateinit var tvActivityData: TextView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvActivityData = findViewById(R.id.tvActivityData) as TextView
        button = findViewById(R.id.button) as Button
        button.setOnClickListener{
            var activity = Activity(activityManager.getNextPrimaryKey(realm), "Test", "RED", Double.MIN_VALUE, false, Type.perHour.toString())
            activityManager.addOrUpdateActivity(realm, activity)
            tvActivityData.text = activityManager.getallActivities(realm).toString()
            tvActivityData.text = Date().toString()
        }
        try {
            Realm.init(this.applicationContext)
            realm = Realm.getDefaultInstance()
            var activityEg = Activity(activityManager.getNextPrimaryKey(realm), "Test", "RED", Double.MIN_VALUE, false, Type.perHour.toString())
            activityManager.addOrUpdateActivity(realm, activityEg)
        } catch (e: Exception){
            Log.e(TAG, "Exceptioin in crating realm object")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun newActivity( v: View) {
        val intent = Intent(this, ActivityEditorActivity::class.java)
        startActivity(intent)
    }
}
