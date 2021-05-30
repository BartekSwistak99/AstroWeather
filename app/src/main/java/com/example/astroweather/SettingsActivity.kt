package com.example.astroweather

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.astroweather.database.LocationDatabase
import com.example.astroweather.database.LocationRepository
import com.example.astroweather.database.LocationViewModel
import com.google.android.material.snackbar.Snackbar
import androidx.lifecycle.ViewModelProvider
import com.example.astroweather.database.Location

class SettingsActivity : AppCompatActivity() {
    private var refreshInterval: Int = 15
    private var refreshIntervalPrev: Int = 30
    private lateinit var locationViewModel :LocationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

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
        refreshIntervalSpinner.adapter = adapter
        val spinnerPosition = adapter.getPosition(compareValue)
        refreshIntervalSpinner.setSelection(spinnerPosition)
                refreshIntervalSpinner.setSelection(1)



        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        val citySpinner = findViewById<Spinner>(R.id.locationList)


        initLocationSpinner(this,citySpinner)






//        citySpinner.onItemSelectedListener = object : OnItemSelectedListener {
//            override fun onItemSelected(
//                parentView: AdapterView<*>?,
//                selectedItemView: View,
//                position: Int,
//                id: Long
//            ) {
//                editText.setText("")
//            }
//
//            override fun onNothingSelected(parentView: AdapterView<*>?) {
//                // your code here
//            }
//        }

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

            val intent = Intent(this@SettingsActivity,InfoActivity::class.java)
            intent.putExtra("latitude",latitude)
            intent.putExtra("longitude",longitude)
            intent.putExtra("refresh",this.refreshInterval)
            startActivity(intent);
        }
        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            val intent = Intent(this@SettingsActivity,InfoActivity::class.java)
            intent.putExtra("latitude",latitudePrev)
            intent.putExtra("longitude",longitudePrev)
            intent.putExtra("refresh",this.refreshIntervalPrev)
            startActivity(intent)
        }
    }
    private fun initLocationSpinner(context: Context,spinner:Spinner){

        val listOfLocations = locationViewModel.getListOfData().observe(this,
            Observer {
                    list ->
                val spinnerAdapter = ArrayAdapter(context,R.layout.spinner_item,list)
                spinner.adapter = spinnerAdapter
        })
    }

    private fun Spinner.initValues(arrays: Int, context: Context) {
        ArrayAdapter.createFromResource(
            context,
            arrays,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            this.adapter = adapter
        }
    }


}