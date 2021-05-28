package com.example.astroweather

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private var refreshInterval: Int = 15
    private var refreshIntervalPrev: Int = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val latitudePrev = intent.getDoubleExtra("latitude", 0.0)
        val longitudePrev = intent.getDoubleExtra("longitude", 0.0)
        this.refreshIntervalPrev = intent.getIntExtra("refresh", 30)
        this.refreshInterval = refreshIntervalPrev

        val latitudeField = findViewById<EditText>(R.id.latitudeText)
        latitudeField.setText(latitudePrev.toString(),TextView.BufferType.EDITABLE)

        val longitudeField = findViewById<EditText>(R.id.longitudeText)
        longitudeField.setText(longitudePrev.toString(),TextView.BufferType.EDITABLE)

        val refreshIntervalSpinner = findViewById<Spinner>(R.id.spinner_refresh_interval)
//        refreshIntervalSpinner.initValues(R.array.refresh_intervals, this)

        val compareValue = refreshInterval.toString()+"s"
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.refresh_intervals,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        refreshIntervalSpinner.setAdapter(adapter)
        if (compareValue != null) {
            val spinnerPosition = adapter.getPosition(compareValue)
            refreshIntervalSpinner.setSelection(spinnerPosition)
        }
//        refreshIntervalSpinner.setSelection(1)




                
        val latitudeSpinner = findViewById<Spinner>(R.id.latitude_spinner)
        latitudeSpinner.initValues(R.array.latitude_array, this)

        val longitudeSpinner = findViewById<Spinner>(R.id.longitude_spinner)
        longitudeSpinner.initValues(R.array.longitude_array, this)
        val parentLayout: View = findViewById(android.R.id.content)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            var latitude = 0.0
            var longitude = 0.0
            try {
                latitude = latitudeField.text.toString().toDouble()
                longitude = longitudeField.text.toString().toDouble()
                if (latitude >90 ||longitude > 90)
                    throw java.lang.NumberFormatException()
            } catch (e: NumberFormatException) {
                Snackbar.make(parentLayout,  R.string.incorrect, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                return@setOnClickListener

            }
            if (latitudeSpinner.selectedItemPosition > 0) {
                latitude = -latitude
            }
            if (longitudeSpinner.selectedItemPosition > 0) {
                longitude = -longitude
            }
            val intervalStr = refreshIntervalSpinner.selectedItem.toString().removeSuffix("s")
            this.refreshInterval = intervalStr.toInt()
            Log.i("sel", "selected:" +intervalStr )

            val intent = Intent(this@MainActivity,InfoActivity::class.java)
            intent.putExtra("latitude",latitude)
            intent.putExtra("longitude",longitude)
            intent.putExtra("refresh",this.refreshInterval)
            startActivity(intent);
        }
        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            val intent = Intent(this@MainActivity,InfoActivity::class.java)
            intent.putExtra("latitude",latitudePrev)
            intent.putExtra("longitude",longitudePrev)
            intent.putExtra("refresh",this.refreshIntervalPrev)
            startActivity(intent)
        }
    }


    private fun Spinner.initValues(arrays: Int, context: Context) {
        ArrayAdapter.createFromResource(
            context,
            arrays,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            this.adapter = adapter
        }
    }
    private fun closeSoftKeyboard(context: Context, v: View) {
        val iMm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        iMm.hideSoftInputFromWindow(v.windowToken, 0)
        v.clearFocus()
    }
}