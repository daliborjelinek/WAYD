package com.example.wayd.activities

import android.content.Intent

import android.os.Bundle
import android.view.PointerIcon
import androidx.appcompat.app.AppCompatActivity

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.wayd.dbmanagersImpl.WAYDManagerImpl


import com.example.wayd.R
import com.example.wayd.ui.RecyclerViewAdapter
import com.example.wayd.dbmanagersImpl.ActivityManagerImpl
import com.example.wayd.dbmanagersImpl.RecordManagerImpl
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG: String = "MainActivity"
    }
    private val activities: ArrayList<TmpActivity> = ArrayList()
    private val activityManager = ActivityManagerImpl()
    private val recordManager = RecordManagerImpl()
    private val WAYDManager = WAYDManagerImpl()
    private lateinit var realm: Realm
    private lateinit var tvActivityData: TextView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // setSupportActionBar(findViewById(R.id.action_bar))

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        recyclerView.adapter = RecyclerViewAdapter(activities,this)

        activities.add(TmpActivity("School","10:00","#f44336","500 $", "&#xf2bb;"))
        activities.add(TmpActivity("Work","19:00","#2196f3","200 $", "&#xf0b1;"))
        activities.add(TmpActivity("Sport","55:30","#009688","500 $", "&#xf45f;"))
        activities.add(TmpActivity("Sport","55:30","#009688","500 $", "&#xf45f;"))
        activities.add(TmpActivity("Work","19:00","#2196f3","200 $", "&#xf0b1;"))
        activities.add(TmpActivity("Sport","55:30","#009688","500 $", "&#xf45f;"))
        activities.add(TmpActivity("Sport","55:30","#009688","500 $", "&#xf45f;"))


    }



    fun newActivity( v: View) {
        val intent = Intent(this, ActivityEditorActivity::class.java)
        startActivity(intent)
    }
}

data class TmpActivity(val name: String, val time: String, val color: String, val value: String,val icon: String)
