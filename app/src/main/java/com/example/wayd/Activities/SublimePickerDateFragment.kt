package com.example.wayd.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.AlphabeticIndex
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.appeaser.sublimepickerlibrary.SublimePicker
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker
import com.example.wayd.dbentities.Record
import com.example.wayd.dbmanagersImpl.RecordManagerImpl
import io.realm.Realm
import java.time.LocalDate
import java.util.*

class SublimePickerDateFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val recordManager = RecordManagerImpl(Realm.getDefaultInstance())
        var mListener = object : SublimeListenerAdapter() {

            override fun onCancelled() {
                // Handle click on `Cancel` button
                dismiss()
            }

            @SuppressLint("NewApi")
            override fun onDateTimeRecurrenceSet(sublimeMaterialPicker: SublimePicker?, selectedDate: SelectedDate?, hourOfDay: Int, minute: Int, recurrenceOption: SublimeRecurrencePicker.RecurrenceOption?, recurrenceRule: String?) {
                var recordFromDb : Record
                if (arguments == null || arguments!!.getLong("recordID") == null || arguments!!.getLong("recordID") == 0L){
                    recordFromDb = Record(
                        recordManager.getNextPrimaryKey(),
                        System.currentTimeMillis(),
                        arguments!!.getLong("activityID"),
                        System.currentTimeMillis()
                    )
                }else {
                    recordFromDb = recordManager.getRecord(arguments!!.getLong("recordID"))
                }



                var record:Record
                if (arguments?.getString("mode") == "end"){
                    record = Record(
                        recordFromDb._ID,
                        recordFromDb.timeStarted,
                        arguments!!.getLong("activityID"),
                        selectedDate!!.firstDate.timeInMillis
                    )
                }else{
                    record = Record(
                        recordFromDb._ID,
                        selectedDate!!.firstDate.timeInMillis,
                        arguments!!.getLong("activityID"),
                        recordFromDb.endTime
                    )
                }
                recurrenceRule?.let{
                    // Do something with recurrenceRule
                }

                recurrenceOption?.let {
                    // Do something with recurrenceOption
                    // Call to recurrenceOption.toString() to get recurrenceOption as a String
                }
                Log.d("Sublime", record.toString())
                recordManager.addOrUpdateRecord(record)
                dismiss()
            }

        }

        var sublimePicker = SublimePicker(context)
        var sublimeOptions = SublimeOptions() // This is optional
        sublimeOptions.setPickerToShow(SublimeOptions.Picker.DATE_PICKER) // I want the recurrence picker to show.
        sublimeOptions.setDisplayOptions(SublimeOptions.ACTIVATE_DATE_PICKER) // I only want the recurrence picker, not the date/time pickers.
        sublimePicker.initializePicker(sublimeOptions,mListener)
        return sublimePicker
    }
}