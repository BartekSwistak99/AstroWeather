package com.example.astroweather

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.astrocalculator.AstroCalculator
import com.astrocalculator.AstroDateTime
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.concurrent.thread

class InfoActivity : AppCompatActivity() {
    private var actualCalendar = Calendar.getInstance()
    private var refreshCalendar = Calendar.getInstance()
    private lateinit var location: AstroCalculator.Location
    private lateinit var moonInfo: AstroCalculator.MoonInfo

    private lateinit var astroCalc: AstroCalculator
    private lateinit var sunInfo: AstroCalculator.SunInfo
    private  var refreshInterval: Int = 30
    private lateinit var moonFragment: MoonFragment
    private lateinit var sunFragment: SunFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title =
            getString(R.string.app_name)


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { _ ->
            val intent = Intent(this,SettingsActivity::class.java)
            intent.putExtra("latitude",location.latitude)
            intent.putExtra("longitude",location.longitude)
            intent.putExtra("refresh",refreshInterval)
            startActivity(intent)
        }
        moonFragment = supportFragmentManager.findFragmentById(R.id.MoonFragment) as MoonFragment
        sunFragment = supportFragmentManager.findFragmentById(R.id.SunFragment) as SunFragment

        refreshInterval = intent.getIntExtra("refresh", refreshInterval)
        refreshCalendar.add(Calendar.SECOND, refreshInterval)


        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)

        val str:String = if(latitude>=0) "$latitude N"
        else "${-latitude} S"
        val str2:String = if(longitude>=0) "$longitude E"
        else "${-longitude} W"

        val locationView = findViewById<TextView>(R.id.locationView)

        locationView.text = "$str $str2"

        initAstro(latitude, longitude)
        updateMoon()
        updateSun()

        thread {
            while (true) {
                Thread.sleep(50)
                val nowCalendar = Calendar.getInstance()
                if (refreshCalendar.get(Calendar.SECOND) == nowCalendar
                        .get(Calendar.SECOND)
                ) {
                    this@InfoActivity.runOnUiThread {
                        astroCalc = AstroCalculator(astroTimeFacotry(nowCalendar),location)
                        refreshCalendar.add(Calendar.SECOND, refreshInterval)
                        sunInfo = astroCalc.sunInfo
                        moonInfo = astroCalc.moonInfo

                        updateMoon()
                        moonFragment.refreshDisplayedInfo()
                        updateSun()
                        sunFragment.refreshDisplayedInfo()
                        Log.i("refresh", "REFRESHED")
                    }
                }
            }
        }


    }

    private fun initAstro(latitude: Double, longitude: Double) {
        this.location = AstroCalculator.Location(latitude, longitude)
        this.astroCalc = AstroCalculator(astroTimeFacotry(actualCalendar), location)
        this.moonInfo = astroCalc.moonInfo
        this.sunInfo = astroCalc.sunInfo
    }

    private fun updateMoon() {
        moonFragment.updateMoonInfo(
            moonInfo.moonrise.toString().removeSuffix(" GMT+1"),
            moonInfo.moonset.toString().removeSuffix(" GMT+1"),
            moonInfo.nextNewMoon.toString().removeSuffix(" GMT+1"),
            moonInfo.nextFullMoon.toString().removeSuffix(" GMT+1"),
            moonInfo.age.toString()
//            String.format("%.2f", moonInfo.age)
        )
    }

    private fun updateSun() {
        sunFragment.updateSunInfo(
            sunInfo.sunrise.toString().removeSuffix(" GMT+1"),
            sunInfo.azimuthRise.toString(),
//            String.format("%.2f", sunInfo.azimuthRise),
            sunInfo.twilightMorning.toString().removeSuffix(" GMT+1"),
            sunInfo.sunset.toString().removeSuffix(" GMT+1"),
            sunInfo.azimuthSet.toString(),
//            String.format("%.2f", sunInfo.azimuthSet),
            sunInfo.twilightEvening.toString().removeSuffix(" GMT+1")
        )
    }

    private fun astroTimeFacotry(calendar: Calendar): AstroDateTime {
        return AstroDateTime(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND),
            1,
            false
        )
    }
}
