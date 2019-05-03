package com.example.WAYD.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.WAYD.DBEntities.Activity
import com.example.WAYD.DBManagersImpl.ActivityManagerImpl
import com.example.WAYD.DBManagersImpl.RecordManagerImpl
import com.example.WAYD.DBManagersImpl.WAYDManagerImpl
import com.example.WAYD.Enums.Color
import com.example.WAYD.Enums.Type
import com.example.WAYD.R
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
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
        tvActivityData = findViewById(R.id.tvActivityData)
        button = findViewById(R.id.button)
        button.setOnClickListener{
            var activity = Activity(activityManager.getNextPrimaryKey(realm), "Test", "RED", Double.MIN_VALUE, false, Type.perHour.toString())
            activityManager.addOrUpdateActivity(realm, activity)
            tvActivityData.text = activityManager.getallActivities(realm).toString()
            tvActivityData.text = Date().toString()
        }
        try {
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
}
