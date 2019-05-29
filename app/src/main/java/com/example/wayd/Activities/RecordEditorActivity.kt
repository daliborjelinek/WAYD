package com.example.wayd.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
    lateinit var buttonEditEndDate : Button
    lateinit var buttonEditStartDate : Button
    lateinit var startDateTv : TextView
    lateinit var endDateTv : TextView
    val recordManager = RecordManagerImpl(Realm.getDefaultInstance())
    val timeFormat = SimpleDateFormat("HH:mm")
    val dateFormat = SimpleDateFormat("yyyy-MM-dd");
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_editor)
        saveButton = findViewById(R.id.buttonSaveRecord)
        buttonEditEndTime = findViewById(R.id.buttonEditEndTime)
        buttonEditEndTime.setOnClickListener {
            editTime("end")
        }
        buttonEditStartTime = findViewById(R.id.buttonEditStartTime)
        buttonEditStartTime.setOnClickListener { editTime("start") }
        buttonEditEndDate = findViewById(R.id.buttonEditEndDate)
        buttonEditEndDate.setOnClickListener {
            editDate("end")
        }
        buttonEditStartDate = findViewById(R.id.buttonEditStartDate)
        buttonEditStartDate.setOnClickListener {
            editDate("start")
        }
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
            endDateTv = findViewById(R.id.endDateTvRecordEditor)
            endDateTv.text = dateFormat.format(recordManager.getRecord(intent.getLongExtra("recordID", 0L)).endTime)
            startDateTv = findViewById(R.id.startDateTvRecordEditor)
            startDateTv.text = dateFormat.format(recordManager.getRecord(intent.getLongExtra("recordID", 0L)).timeStarted)
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
            endDateTv = findViewById(R.id.endDateTvRecordEditor)
            endDateTv.text = dateFormat.format(recordManager.getRecord(intent.getLongExtra("recordID", 0L)).endTime)
            startDateTv = findViewById(R.id.startDateTvRecordEditor)
            startDateTv.text = dateFormat.format(recordManager.getRecord(intent.getLongExtra("recordID", 0L)).timeStarted)
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

    fun editTime(mode:String) {
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

    fun editDate(mode:String){
        val fragmentManager = supportFragmentManager
        var sublimePickerDateFormat = SublimePickerDateFragment()
        var bundle = Bundle()
        Log.d("Sublime", intent.getLongExtra("recordID", 0L).toString())
        bundle.putLong("recordID", intent.getLongExtra("recordID", 0L))
        bundle.putLong("activityID", intent.getLongExtra("activityID", 0L))
        bundle.putString("mode", mode)
        sublimePickerDateFormat.arguments = bundle
        sublimePickerDateFormat.isCancelable = false
        sublimePickerDateFormat.show(fragmentManager,null)
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