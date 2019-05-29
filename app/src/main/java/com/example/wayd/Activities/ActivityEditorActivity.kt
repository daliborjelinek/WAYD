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
import com.example.wayd.dbmanagersImpl.ActivityManagerImpl

import io.realm.Realm
import kotlinx.android.synthetic.main.activity_editor.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.wayd.UI.IconTextView
import com.example.wayd.enums.Type
import com.yarolegovich.lovelydialog.LovelyStandardDialog

import android.widget.Spinner
import com.example.wayd.WAYD
import com.example.wayd.dbmanagersImpl.RecordManagerImpl
import com.example.wayd.dbmanagersImpl.WAYDManagerImpl


class ActivityEditorActivity : AppCompatActivity() {
    private lateinit var editorHeader: ConstraintLayout
    private lateinit var perActivityOption: RadioButton
    private lateinit var perHourOption: RadioButton
    private lateinit var nameEditText: EditText
    private lateinit var valueEditText: EditText
    private lateinit var colorSpinner: Spinner
    private lateinit var iconSpinner: Spinner
    private lateinit var activityManager: ActivityManagerImpl
    private lateinit var buttonViewRecord: Button
    private lateinit var buttonDeleteActivity: Button
    private lateinit var iconTextView: IconTextView
    private var unchangedActivity: Activity? = null
    private var selectedActivityType = Type.perActivity.name
    private val waydManager = WAYDManagerImpl(Realm.getDefaultInstance())
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
        iconSpinner = findViewById(R.id.spinnerIcons)
        buttonDeleteActivity = findViewById(R.id.buttonDelete)
        buttonViewRecord = findViewById(R.id.buttonViewRecords)
        iconTextView = findViewById(R.id.activityIconTextView)
        unchangedActivity =  activityManager.getActivity(intent.getLongExtra("activityID", 0))
        if (unchangedActivity != null){
            nameEditText.setText(unchangedActivity?.name)
            valueEditText.setText(unchangedActivity?.value.toString())
            Log.d(TAG, unchangedActivity?.running.toString())
            if(unchangedActivity!!.type == Type.perActivity.name){
                    perActivityOption.isChecked = true
            }
            else{
                perHourOption.isChecked = true

            }

            spinnerIcons.setSelection((getSpinnerIndex(spinnerIcons,unchangedActivity!!.icon)))
            spinnerColors.setSelection((getSpinnerIndex(spinnerColors,unchangedActivity!!.color)))


        }
        else{
            buttonViewRecord.visibility = View.INVISIBLE
            buttonDeleteActivity.visibility = View.INVISIBLE
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


    fun tryDeleteActivity( v: View){
        LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
            .setTopColor(Color.parseColor(unchangedActivity!!.color))
            .setIcon(getDrawable(R.drawable.ic_trash))
            .setTitle("Delete selected activity")
            .setMessage("Do you really want to delete this activity?")
            .setPositiveButton("YES",
                View.OnClickListener {
                    waydManager.safeDeleteActivity(unchangedActivity!!)
                    switchToMainActivity()
                })
            .setNegativeButton("NO", null)
            .show()
    }



    fun onGoBackClicked( view: View) {
        switchToMainActivity()
    }

    fun onSaveClicked( view: View) {
        if (nameEditText.text.isEmpty() or valueEditText.text.isEmpty()){
            LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColor(Color.parseColor(colorSpinner.selectedItem.toString()))
                .setTitle("No Data provided")
                .setMessage("Name and value cannot be empty")
                .setNegativeButton("OK", null)
                .show()
            return
        }
        var primaryKey = intent.getLongExtra("activityID", 0 )
        if (primaryKey == 0L){
            primaryKey = activityManager.getNextPrimaryKey()
        }
        selectedActivityType = if (perActivityOption.isChecked) Type.perActivity.name
        else Type.perHour.name
        activityManager.addOrUpdateActivity(
            Activity(
                primaryKey,
                nameEditText.text.toString(),
                colorSpinner.selectedItem.toString(),
                valueEditText.text.toString().toDouble(),
                false,
                selectedActivityType,
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

    //private method of your class
    private fun getSpinnerIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }

        return 0
    }

    fun switchToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun switchToRecordListActivity(){
        val intent = Intent(this, RecordListActivity::class.java)
        if (unchangedActivity != null) {
            intent.putExtra("activityID", unchangedActivity?._ID)
        }
        Log.d("TAG", unchangedActivity?._ID.toString())
        startActivity(intent)
    }

    fun setUpViewRecords(){
        buttonViewRecord.setOnClickListener(){
            switchToRecordListActivity()
        }
    }

}
