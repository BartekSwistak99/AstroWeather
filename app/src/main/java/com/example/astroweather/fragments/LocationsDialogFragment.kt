package com.example.astroweather.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.astroweather.InternetConnection
import com.example.astroweather.R
import com.example.astroweather.database.WeatherViewModel

class LocationsDialogFragment(val weathersViewModel: WeatherViewModel) : DialogFragment() {
    var dismissFlag = true
    lateinit var entryView: EditText
    lateinit var connectionError: TextView
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            //TODO buttons onclick
            val view = inflater.inflate(R.layout.fragment_dialog_locations, null)
            connectionError = view.findViewById(R.id.internet_error)
            if (InternetConnection.isOnline(it))
                connectionError.visibility = View.INVISIBLE
            else{
                connectionError.text = getText(R.string.no_internet)
                connectionError.visibility = View.VISIBLE

            }
            entryView = view.findViewById(R.id.locationEdit)


            builder.setView(view)
                .setPositiveButton(
                    R.string.add, null
                )

                .setNegativeButton(R.string.button_cancel) { dialog, id -> dialog.dismiss() }


            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart() {
        super.onStart()
        (dialog as AlertDialog?)!!.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            try {
                InternetConnection.getWeatherByCityName(
                    entryView.text.toString(),
                    weathersViewModel
                )
                dialog?.dismiss()
            } catch (e: Exception) {
                entryView.setText("")
                connectionError.text = "City not found!"
                connectionError.visibility = View.VISIBLE
            }
        }
    }

}