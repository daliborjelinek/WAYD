package com.example.wayd.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.wayd.activities.SublimePickerDialogFragment
import com.example.wayd.R
import com.example.wayd.dbentities.Record
import com.example.wayd.dbmanagersImpl.RecordManagerImpl
import com.yarolegovich.lovelydialog.LovelyStandardDialog
import io.realm.Realm
import java.text.SimpleDateFormat

class RecordEditorActivity : AppCompatActivity(){
    lateinit var saveButton : Button
    lateinit var startTimeTv : TextView
    lateinit var endTimeTv : TextView
    lateinit var buttonEditEndTime : Button
    lateinit var buttonEditStartTime : Button
    val recordManager = RecordManagerImpl(Realm.getDefaultInstance())
    val timeFormat = SimpleDateFormat("HH:mm")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_editor)
        saveButton = findViewById(R.id.buttonSaveRecord)
        buttonEditEndTime = findViewById(R.id.buttonEditEndTime)
        buttonEditEndTime.setOnClickListener {
            editDate("end")
        }
        buttonEditStartTime = findViewById(R.id.buttonEditStartTime)
        buttonEditStartTime.setOnClickListener { editDate("start") }
        saveButton.setOnClickListener{
            var primaryKey = intent.getLongExtra("recordID", 0L)
            Log.d("RecordId", intent.getLongExtra("recordID", 0L).toString())
            if (primaryKey == 0L){
                primaryKey = recordManager.getNextPrimaryKey()
            }
            val dbRecord = recordManager.getRecord(primaryKey)
            val record = Record(
                primaryKey,
                dbRecord.timeStarted,
                intent.getLongExtra("activityID", 0L),
                dbRecord.endTime
            )
            Log.d("TAG", record._ID.toString()+ " "+record.endTime + " " + record.activityId )
            recordManager.addOrUpdateRecord(
                record
            )
            val newIntent = Intent(this, RecordListActivity::class.java)
            newIntent.putExtra("activityID", intent.getLongExtra("activityID", 0L))
            startActivity(newIntent)
        }
        if (intent.getLongExtra("recordID", 0L) != 0L ){
            startTimeTv = findViewById(R.id.startTimeTvRecordEditor)
            startTimeTv.text = timeFormat.format(recordManager.getRecord(intent.getLongExtra("recordID", 0L)).timeStarted)
            endTimeTv = findViewById(R.id.endTimeTvRecordEditor)
            endTimeTv.text = timeFormat.format(recordManager.getRecord(intent.getLongExtra("recordID", 0L)).endTime)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Log.d("Focus", intent.toString())
        if (intent.getLongExtra("recordID", 0L) != 0L ){
            startTimeTv = findViewById(R.id.startTimeTvRecordEditor)
            startTimeTv.text = timeFormat.format(recordManager.getRecord(intent.getLongExtra("recordID", 0L)).timeStarted)
            endTimeTv = findViewById(R.id.endTimeTvRecordEditor)
            endTimeTv.text = timeFormat.format(recordManager.getRecord(intent.getLongExtra("recordID", 0L)).endTime)
        }

    }


    fun onSaveClicked(v: View){
        val newIntent = Intent(this, RecordListActivity::class.java)
        newIntent.putExtra("activityID", intent.getLongExtra("activityID", 0L))
        startActivity(newIntent)
    }

    fun onGoBackClicked(v: View) {
        val newIntent = Intent(this, MainActivity::class.java)
        newIntent.putExtra("activityID", intent.getLongExtra("activityID", 0L))
        startActivity(newIntent)
    }

    fun editDate(mode:String) {
        val fragmentManager = supportFragmentManager
        var sublimePickerDialogFragment = SublimePickerDialogFragment()
        var bundle = Bundle()
        Log.d("Sublime", intent.getLongExtra("recordID", 0L).toString())
        bundle.putLong("recordID", intent.getLongExtra("recordID", 0L))
        bundle.putLong("activityID", intent.getLongExtra("activityID", 0L))
        bundle.putString("mode", mode)
// put arguments into bundle
        sublimePickerDialogFragment.arguments = bundle
        sublimePickerDialogFragment.isCancelable = false
        sublimePickerDialogFragment.show(fragmentManager,null)
    }

    fun tryDeleteRecord( v: View){
        LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
            .setTopColorRes(R.color.design_default_color_primary_dark)
            .setIcon(getDrawable(R.drawable.ic_trash))
            .setTitle("Delete selected record")
            .setMessage("Do you really want to delete this record?")
            .setPositiveButton("YES",
                View.OnClickListener {
                    recordManager.deleteRecord(intent.getLongExtra("recordID", 0L))
                    val newIntent = Intent(this, RecordListActivity::class.java)
                    newIntent.putExtra("activityID", intent.getLongExtra("activityID", 0L))
                    startActivity(newIntent)
                })
            .setNegativeButton("NO", null)
            .show()
    }



}