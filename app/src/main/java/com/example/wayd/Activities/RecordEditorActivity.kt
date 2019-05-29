package com.example.wayd.activities

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.wayd.activities.SublimePickerDialogFragment
import com.example.wayd.R
import com.example.wayd.dbentities.Record
import com.example.wayd.dbmanagersImpl.RecordManagerImpl
import com.yarolegovich.lovelydialog.LovelyStandardDialog
import io.realm.Realm

class RecordEditorActivity : AppCompatActivity(){
    // lateinit var startTimePicker:TimePicker
    // lateinit var endTimePicker:TimePicker
    lateinit var saveButton : Button
    val recordManager = RecordManagerImpl(Realm.getDefaultInstance())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_editor)
        // startTimePicker= findViewById(R.id.startTimeTimePicker)
        //startTimePicker.setIs24HourView(true)
        //endTimePicker= findViewById(R.id.endTimeTimePicker)
        //endTimePicker.setIs24HourView(true)
        saveButton = findViewById(R.id.buttonSaveRecord)
        if (intent.getLongExtra("recordID", 0L) != 0L){
          //  startTimePicker.currentHour = 1
          //  startTimePicker.currentMinute = 1
        }
        saveButton.setOnClickListener{
            var primaryKey = intent.getLongExtra("recordID", 0L)
            if (primaryKey == 0L){
                primaryKey = recordManager.getNextPrimaryKey()
            }
            Log.d("ActivityID", intent.getLongExtra("activityID", 0L).toString())
            val record = Record(
                primaryKey,
            //    timePickerTimeToMilis(startTimePicker),
                10, //TODO: hardcoded for testing
                intent.getLongExtra("activityID", 0L),
             //   timePickerTimeToMilis(endTimePicker)
            10
            )
            Log.d("TAG", record._ID.toString()+ " "+record.endTime + " " + record.activityId )
            recordManager.addOrUpdateRecord(
                record
            )
        }

    }
    fun timePickerTimeToMilis(timePicker: TimePicker):Long{
        return timePicker.currentHour*60*60*1000+timePicker.currentMinute*60*1000L
    }

    fun onSaveClicked(v: View){
            //TODO
    }

    fun onGoBackClicked(v: View) {
            //TODO
    }

    fun editDate(v: View) {
        val fragmentManager = supportFragmentManager
        var sublimePickerDialogFragment = SublimePickerDialogFragment()
        var bundle = Bundle()
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
                  //TODO
                })
            .setNegativeButton("NO", null)
            .show()
    }



}