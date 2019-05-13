package com.example.wayd.activities

import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.PointerIcon
import androidx.appcompat.app.AppCompatActivity

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.wayd.dbmanagersImpl.WAYDManagerImpl


import com.example.wayd.R
import com.example.wayd.dbentities.Activity
import com.example.wayd.dbmanagersImpl.ActivityManagerImpl
import com.example.wayd.dbmanagersImpl.RecordManagerImpl
import com.example.wayd.ui.RecyclerViewAdapterActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG: String = "MainActivity"
    }
    private var activities: ArrayList<Activity> = ArrayList()
    private val activityManager = ActivityManagerImpl(Realm.getDefaultInstance())
    private val recordManager = RecordManagerImpl(Realm.getDefaultInstance())
    private val WAYDManager = WAYDManagerImpl(Realm.getDefaultInstance())
    private lateinit var realm: Realm
    private lateinit var tvActivityData: TextView
    private lateinit var button: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, activityManager.getallActivities().toString())
        activities.addAll(activityManager.getallActivities())
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val rvAdapter = RecyclerViewAdapterActivity(activities,this)
        rvAdapter.onItemClick = {activity ->
            newActivity(activity._ID)
        }
        recyclerView.adapter = rvAdapter

        button = findViewById(R.id.floatingDeleteButton)
        button.setOnClickListener{
            activityManager.deleteAllActivities()
            recordManager.deleteAllRecords()
            activities.clear()
        }




    }



    fun newActivity(activityId: Long) {
        val intent = Intent(this, ActivityEditorActivity::class.java)
        intent.putExtra("activityID", activityId)
        startActivity(intent)
    }

    fun newActivity(v:View) {
        val intent = Intent(this, ActivityEditorActivity::class.java)
        startActivity(intent)
    }
}
