package com.example.astroweather.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.astroweather.InternetConnection
import com.example.astroweather.R
import com.example.astroweather.database.forecast.ForecastViewModel
import com.example.astroweather.database.weather.WeatherViewModel
import com.example.astroweather.fragments.LocationsDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class SettingsActivity : AppCompatActivity() {
    private var refreshInterval: Int = 15
    private var refreshIntervalPrev: Int = 30
    private lateinit var weathersViewModel: WeatherViewModel
    private lateinit var forecastsViewModel: ForecastViewModel
    private var context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val latitudePrev = intent.getDoubleExtra("latitude", 0.0)
        val longitudePrev = intent.getDoubleExtra("longitude", 0.0)
        this.refreshIntervalPrev = intent.getIntExtra("refresh", 30)
        this.refreshInterval = refreshIntervalPrev

        val latitudeField = findViewById<EditText>(R.id.latitudeText)
        latitudeField.setText(latitudePrev.toString(), TextView.BufferType.EDITABLE)
        val longitudeField = findViewById<EditText>(R.id.longitudeText)
        longitudeField.setText(longitudePrev.toString(), TextView.BufferType.EDITABLE)

        val refreshIntervalSpinner = findViewById<Spinner>(R.id.spinner_refresh_interval)
//        refreshIntervalSpinner.initValues(R.array.refresh_intervals, this)

        val compareValue = refreshInterval.toString() + "s"
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



        weathersViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        forecastsViewModel = ViewModelProvider(this).get(ForecastViewModel::class.java)

        thread {
            val connectionError: TextView = findViewById(R.id.internet_error)
            while (true) {
                this@SettingsActivity.runOnUiThread{
                    if (InternetConnection.isOnline(context))
                        connectionError.visibility = View.INVISIBLE
                    else
                        connectionError.visibility = View.VISIBLE
                }

                sleep(1000)
            }
        }
        val locationSpinner = findViewById<Spinner>(R.id.locationList)
        initLocationSpinner(this, locationSpinner)

        val addNewButton = findViewById<ImageButton>(R.id.button_add_new)
        addNewButton.setOnClickListener {
            if (!InternetConnection.isOnline(this)) {
                val errorBuilder = AlertDialog.Builder(this)
                errorBuilder.setTitle("No connection")
                errorBuilder.setMessage("check your internet connection")
                errorBuilder.setPositiveButton("ok", { dialog, which -> dialog.cancel() })

                errorBuilder.show()
            } else {
                val locationFragment = LocationsDialogFragment(weathersViewModel,forecastsViewModel)
                locationFragment.show(supportFragmentManager, "tag")
            }

        }
        val latitudeSpinner = findViewById<Spinner>(R.id.latitude_spinner)
        latitudeSpinner.initValues(R.array.latitude_array, this)
        val longitudeSpinner = findViewById<Spinner>(R.id.longitude_spinner)
        longitudeSpinner.initValues(R.array.longitude_array, this)
        val fav:Button = findViewById(R.id.buttonfav)
        fav.setOnClickListener {
            var latitude = 0.0
            var longitude = 0.0
            try {
                latitude = latitudeField.text.toString().toDouble()
                longitude = longitudeField.text.toString().toDouble()
                if (latitude > 90 || longitude > 90)
                    throw java.lang.NumberFormatException()
            } catch (e: NumberFormatException) {
                Toast.makeText(this,R.string.incorrect,Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (latitudeSpinner.selectedItemPosition > 0) {
                latitude = -latitude
            }
            if (longitudeSpinner.selectedItemPosition > 0) {
                longitude = -longitude
            }
            InternetConnection.downloadWeatherByCords(latitude,longitude,weathersViewModel,this,isFavourite = true)
            InternetConnection.downloadForecastByCords(latitude,longitude,forecastsViewModel,this)
            Toast.makeText(this,"added to favourites",Toast.LENGTH_SHORT).show()
        }
        val delete:Button = findViewById(R.id.buttondelete)
        delete.setOnClickListener {
            val selectedCity:String = locationSpinner.selectedItem.toString()

            if(selectedCity =="none"){
                Toast.makeText(this,"city not selected!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val weather = weathersViewModel.getLocationByName(selectedCity)
            weather?.isUserFavourite = false
            initLocationSpinner(this, locationSpinner)

            Toast.makeText(this,"city ${weather?.name} deleted",Toast.LENGTH_SHORT).show()

        }


        val parentLayout: View = findViewById(android.R.id.content)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            var latitude = 0.0
            var longitude = 0.0
            try {
                latitude = latitudeField.text.toString().toDouble()
                longitude = longitudeField.text.toString().toDouble()
                if (latitude > 90 || longitude > 90)
                    throw java.lang.NumberFormatException()
            } catch (e: NumberFormatException) {
                Snackbar.make(parentLayout, R.string.incorrect, Snackbar.LENGTH_LONG)
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
            Log.i("sel", "selected:" + intervalStr)

            val selectedCity:String = locationSpinner.selectedItem.toString()
            val unitSwitch:SwitchCompat = findViewById(R.id.unit_switch)
            val isCelsius:Boolean = !unitSwitch.isChecked

            if(selectedCity !="none"){
                val weather = weathersViewModel.getLocationByName(selectedCity)
                latitude = weather?.coord?.lat ?: 0.0
                longitude = weather?.coord?.lon ?: 0.0
            }
            else{
              InternetConnection.downloadWeatherByCords(latitude,longitude,weathersViewModel,this)
              InternetConnection.downloadForecastByCords(latitude,longitude,forecastsViewModel,this)
            }

            val intent = Intent(this@SettingsActivity, InfoActivity::class.java)
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("refresh", this.refreshInterval)
            intent.putExtra("isCelsius",isCelsius)
            intent.putExtra("selectedCity", selectedCity)
            startActivity(intent);
        }
        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            val intent = Intent(this@SettingsActivity, InfoActivity::class.java)
            intent.putExtra("latitude", latitudePrev)
            intent.putExtra("longitude", longitudePrev)
            intent.putExtra("refresh", this.refreshIntervalPrev)
            intent.putExtra("isCelsius",true)
            intent.putExtra("selectedCity", "none")
            startActivity(intent)
        }

        val refresh :ImageView = findViewById(R.id.refresh)
        refresh.setOnClickListener {
            val selectedCity:String = locationSpinner.selectedItem.toString()
            if(selectedCity != "none"){
                InternetConnection.downloadWeatherByCityName(selectedCity,weathersViewModel,false,this)
                InternetConnection.downloadForecastByCityName(selectedCity,forecastsViewModel,this)
                Toast.makeText(this,"weather refreshed",Toast.LENGTH_SHORT).show()
            }
            else{
                var latitude = latitudeField.text.toString().toDouble()
                var longitude = longitudeField.text.toString().toDouble()
                InternetConnection.downloadWeatherByCords(latitude,longitude,weathersViewModel,this)
                InternetConnection.downloadForecastByCords(latitude,longitude,forecastsViewModel,this)
                Toast.makeText(this,"weather refreshed",Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun initLocationSpinner(context: Context, spinner: Spinner) {

        val listOfWeathers = weathersViewModel.getAllWeathers().observe(this,
            Observer {
//                    list ->
                var locationList: MutableList<String>? = weathersViewModel.getAllFavouriteLocations()?.toMutableList()
                if (locationList == null) {
                    locationList = emptyList<String>().toMutableList()
                }
                locationList.add(0,"none")

                val spinnerAdapter = ArrayAdapter(context, R.layout.spinner_item, locationList)
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