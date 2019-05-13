package com.example.wayd.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import com.example.wayd.activities.RecordListActivity
import com.example.wayd.R
import com.example.wayd.dbentities.Activity
import com.example.wayd.dbentities.Record
import com.example.wayd.dbmanagersImpl.ActivityManagerImpl
import com.example.wayd.dbmanagersImpl.RecordManagerImpl
import io.realm.Realm
import java.text.SimpleDateFormat

class ActivityEditorActivity : AppCompatActivity() {
    private lateinit var buttonSave: Button
    private lateinit var nameEditText: EditText
    private lateinit var valueEditText: EditText
    private lateinit var colorSpinner: Spinner
    private lateinit var iconSpinner: Spinner
    private lateinit var switchRunning: Switch
    private lateinit var typeSpinner: Spinner
    private lateinit var activityManager: ActivityManagerImpl
    private lateinit var buttonViewRecord: Button
    private var unchangedActivity: Activity? = null
    private var recordManager = RecordManagerImpl(Realm.getDefaultInstance())
    companion object {
        const val TAG: String = "EditorActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        setUpManagers()
        setUpUiComponents()
    }


    fun setUpUiComponents(){

        buttonSave = findViewById(R.id.buttonSave)
        setUpOnCreateButtonClick()
        nameEditText = findViewById(R.id.nameEditText)
        valueEditText = findViewById(R.id.valueEditText)
        colorSpinner = findViewById(R.id.spinnerColors)
        typeSpinner = findViewById(R.id.spinnerActivityType)
        iconSpinner = findViewById(R.id.spinnerIcons)
        switchRunning = findViewById(R.id.runningSwitch)
        buttonViewRecord = findViewById(R.id.buttonViewRecords)
        unchangedActivity =  activityManager.getActivity(intent.getLongExtra("activityID", 0))
        if (unchangedActivity != null){
            nameEditText.setText(unchangedActivity?.name)
            valueEditText.setText(unchangedActivity?.value.toString())
            Log.d(TAG, unchangedActivity?.running.toString())
            if (unchangedActivity?.running!!) {
                switchRunning.toggle()
            }
        }
        setUpViewRecords()
    }

    fun setUpOnCreateButtonClick(){
        buttonSave.setOnClickListener(){
            var primaryKey = intent.getLongExtra("activityID", 0 )
            if (primaryKey == 0L){
                 primaryKey = activityManager.getNextPrimaryKey()
            }
            activityManager.addOrUpdateActivity(
                Activity(
                    primaryKey,
                    nameEditText.text.toString(),
                    mapColor(colorSpinner.selectedItem.toString()),
                    valueEditText.text.toString().toDouble(),
                    switchRunning.isChecked,
                    typeSpinner.selectedItem.toString(),
                    iconSpinner.selectedItem.toString()
            ))
            if(switchRunning.isChecked){
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                recordManager.addOrUpdateRecord(
                    Record(
                        recordManager.getNextPrimaryKey(),
                        System.currentTimeMillis(),
                        primaryKey,
                        0L
                    )
                )
            }
            switchToMainActivity()
        }
    }

    fun setUpManagers(){
        try {
            val realm = Realm.getDefaultInstance()
            activityManager = ActivityManagerImpl(realm)
        } catch (e: Exception){
            Log.e(TAG, "Exception in creating realm object")
        }
    }

    fun mapColor(color:String):String{
        when(color.toUpperCase()){
            "RED" ->  return "#f44336"
            "BLUE" -> return "#2196f3"
            "GREEN" -> return "#009688"
        }
        return ""
    }

    fun switchToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun switchToRecordEditorActivity(){
        val intent = Intent(this, RecordListActivity::class.java)
        if (unchangedActivity != null) {
            intent.putExtra("activityID", unchangedActivity?._ID)
        }
        Log.d("TAG", unchangedActivity?._ID.toString())
        startActivity(intent)
    }

    fun setUpViewRecords(){
        buttonViewRecord.setOnClickListener(){
            switchToRecordEditorActivity()
        }
    }

}
