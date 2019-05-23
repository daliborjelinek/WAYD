package com.example.wayd.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.wayd.R
import com.example.wayd.dbentities.Activity
import com.example.wayd.dbentities.Record
import com.example.wayd.dbmanagersImpl.ActivityManagerImpl
import com.example.wayd.dbmanagersImpl.RecordManagerImpl
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_editor.*
import java.text.SimpleDateFormat
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.wayd.UI.IconTextView


class ActivityEditorActivity : AppCompatActivity() {
    //private lateinit var buttonSave: Button
    private lateinit var editorHeader: ConstraintLayout
    private lateinit var perActivityOption: RadioButton
    private lateinit var perHourOption: RadioButton
    private lateinit var nameEditText: EditText
    private lateinit var valueEditText: EditText
    private lateinit var colorSpinner: Spinner
    private lateinit var iconSpinner: Spinner
    private lateinit var switchRunning: Switch
    private lateinit var activityManager: ActivityManagerImpl
    private lateinit var buttonViewRecord: Button
    private lateinit var iconTextView: IconTextView
    private var unchangedActivity: Activity? = null
    private var recordManager = RecordManagerImpl(Realm.getDefaultInstance())
    private var selectedActivityType = "Value per Activity"
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


        editorHeader = findViewById(R.id.activityEditorHeader)
        perActivityOption = findViewById(R.id.perActivityRadioButton)
        perActivityOption.isChecked = true
        perHourOption = findViewById(R.id.perHourRadioButton)
        nameEditText = findViewById(R.id.nameEditText)
        valueEditText = findViewById(R.id.valueEditText)
        colorSpinner = findViewById(R.id.spinnerColors)
       // typeSpinner = findViewById(R.id.spinnerActivityType)
        iconSpinner = findViewById(R.id.spinnerIcons)

        buttonViewRecord = findViewById(R.id.buttonViewRecords)
        iconTextView = findViewById(R.id.activityIconTextView)
        unchangedActivity =  activityManager.getActivity(intent.getLongExtra("activityID", 0))
        if (unchangedActivity != null){
            nameEditText.setText(unchangedActivity?.name)
            valueEditText.setText(unchangedActivity?.value.toString())
            Log.d(TAG, unchangedActivity?.running.toString())
            if (unchangedActivity?.running!!) {
             //   switchRunning.toggle()
            }
        }

        setUpViewRecords()

        spinnerIcons.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    iconTextView.text = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        spinnerColors.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                editorHeader.setBackgroundColor(Color.parseColor(parent.getItemAtPosition(position).toString()))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


    }

//    fun setUpOnCreateButtonClick(){
//        buttonSave.setOnClickListener(){
//            var primaryKey = intent.getLongExtra("activityID", 0 )
//            if (primaryKey == 0L){
//                 primaryKey = activityManager.getNextPrimaryKey()
//            }
//            activityManager.addOrUpdateActivity(
//                Activity(
//                    primaryKey,
//                    nameEditText.text.toString(),
//                    mapColor(colorSpinner.selectedItem.toString()),
//                    valueEditText.text.toString().toDouble(),
//                    false,
//                   "Value per activity",
//                    iconSpinner.selectedItem.toString()
//            ))
//
//            switchToMainActivity()
//        }
//    }

    fun onGoBackClicked( view: View) {
        switchToMainActivity()
    }

    fun onSaveClicked( view: View) {
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
                false,
                "Value per activity",
                iconSpinner.selectedItem.toString()
            ))

        switchToMainActivity()

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
