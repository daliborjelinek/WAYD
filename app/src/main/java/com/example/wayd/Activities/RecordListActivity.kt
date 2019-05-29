package com.example.wayd.activities

import android.content.Intent
import android.icu.text.AlphabeticIndex
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView
import com.example.wayd.R
import com.example.wayd.activities.ActivityEditorActivity
import com.example.wayd.dbentities.Activity
import com.example.wayd.dbentities.Record
import com.example.wayd.dbmanagersImpl.ActivityManagerImpl
import com.example.wayd.dbmanagersImpl.RecordManagerImpl
import com.example.wayd.dbmanagersImpl.WAYDManagerImpl
import com.example.wayd.ui.RecyclerViewAdapterActivity
import com.example.wayd.ui.RecyclerViewAdapterRecord
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*


class RecordListActivity : AppCompatActivity() {
    private var records: ArrayList<Record> = ArrayList()
    private val activityManager = ActivityManagerImpl(Realm.getDefaultInstance())
    private val recordManager = RecordManagerImpl(Realm.getDefaultInstance())
    private val WAYDManager = WAYDManagerImpl(Realm.getDefaultInstance())
    private lateinit var newRecordButton : FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_record_list)
        val activity = activityManager.getActivity(intent.getLongExtra("activityID", 0L))
        records.addAll(WAYDManager.getAllRecordToActivity(activity!!))

        records.reverse()
        Log.d("Records", records.toString())
        val recyclerView:RecyclerView = findViewById(R.id.recyclerViewRecord)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val rvAdapter = RecyclerViewAdapterRecord(records,this)
        rvAdapter.onItemClick = {record ->
            newActivity(record._ID, activity._ID)
        }
        recyclerView.adapter = rvAdapter
        newRecordButton = findViewById(R.id.floatingActionButtonRecord)
        newRecordButton.setOnClickListener {
         //   val newIntent = Intent(this, RecordListActivity::class.java)
            val recordId = recordManager.getNextPrimaryKey()
        //    newIntent.putExtra("recordID", intent.getLongExtra("recordID", recordId))
            recordManager.addOrUpdateRecord(Record(
                recordId,
                System.currentTimeMillis(),
                intent.getLongExtra("activityID", 0L),
                System.currentTimeMillis()
            ))
        //    newIntent.putExtra("activityID", intent.getLongExtra("activityID", 0L))
         //   startActivity(newIntent)
            records.add(0, recordManager.getRecord(recordId))
            rvAdapter.notifyDataSetChanged()
        }

    }

    fun newActivity(recordId: Long, activityId: Long) {
        val intent = Intent(this, RecordEditorActivity::class.java)
        Log.d("TAG", activityId.toString() + " " + recordId)
        intent.putExtra("recordID", recordId)
        intent.putExtra("activityID", activityId)
        startActivity(intent)
    }

    fun newActivity(v: View) {
        val intent = Intent(this, RecordEditorActivity::class.java)
        startActivity(intent)
    }

    fun onGoBackClicked(v: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
