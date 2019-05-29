package com.example.wayd.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.appeaser.sublimepickerlibrary.SublimePicker
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker

class SublimePickerDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var mListener = object : SublimeListenerAdapter() {

            override fun onCancelled() {
                // Handle click on `Cancel` button
                dismiss()
            }

            override fun onDateTimeRecurrenceSet(sublimeMaterialPicker: SublimePicker?, selectedDate: SelectedDate?, hourOfDay: Int, minute: Int, recurrenceOption: SublimeRecurrencePicker.RecurrenceOption?, recurrenceRule: String?) {

                recurrenceRule?.let{
                    // Do something with recurrenceRule
                }

                recurrenceOption?.let {
                    // Do something with recurrenceOption
                    // Call to recurrenceOption.toString() to get recurrenceOption as a String
                }
            }
        }

        var sublimePicker = SublimePicker(context)
        var sublimeOptions = SublimeOptions() // This is optional
        sublimeOptions.setPickerToShow(SublimeOptions.Picker.TIME_PICKER) // I want the recurrence picker to show.
        sublimeOptions.setDisplayOptions(SublimeOptions.ACTIVATE_TIME_PICKER) // I only want the recurrence picker, not the date/time pickers.
        sublimePicker.initializePicker(sublimeOptions,mListener)
        return sublimePicker
    }
}