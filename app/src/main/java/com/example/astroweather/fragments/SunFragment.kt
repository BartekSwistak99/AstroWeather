package com.example.astroweather.fragments

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.astroweather.R


class SunFragment : Fragment(R.layout.fragment_sun) {
    private var sunriseTime: String = ""
    private var sunriseAzimuth: String = ""
    private var sunriseCivilTime: String = ""
    private var twilightTime: String = ""
    private var twilightAzimuth: String = ""
    private var twilightCivilTime: String = ""

    private lateinit var sunriseTimeView: TextView
    private lateinit var sunriseAzimuthView: TextView
    private lateinit var sunriseCivilTimeView: TextView
    private lateinit var twilightTimeView: TextView
    private lateinit var twilightAzimuthView: TextView
    private lateinit var twilightCivilTimeView: TextView


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sunriseTimeView = view!!.findViewById(R.id.sunrise_time)
        sunriseAzimuthView = view!!.findViewById(R.id.sunrise_azimuth)
        sunriseCivilTimeView = view!!.findViewById(R.id.sunrise_civil)
        twilightTimeView = view!!.findViewById(R.id.twilight_time)
        twilightAzimuthView = view!!.findViewById(R.id.twilight_azimuth)
        twilightCivilTimeView = view!!.findViewById(R.id.twilight_civil)
        refreshDisplayedInfo()
    }

    fun updateSunInfo(
        sunriseTime: String,
        sunriseAzimuth: String,
        sunriseCivilTime: String,
        twilightTime: String,
        twilightAzimuth: String,
        twilightCivilTime: String
    ) {

        this.sunriseTime= sunriseTime
        this.sunriseAzimuth= sunriseAzimuth
        this.sunriseCivilTime= sunriseCivilTime
        this.twilightTime= twilightTime
        this.twilightAzimuth= twilightAzimuth
        this.twilightCivilTime= twilightCivilTime
    }

    fun refreshDisplayedInfo() {
        sunriseTimeView.text = sunriseTime
        sunriseAzimuthView.text = sunriseAzimuth
        sunriseCivilTimeView.text = sunriseCivilTime
        twilightTimeView.text = twilightTime
        twilightAzimuthView.text = twilightAzimuth
        twilightCivilTimeView.text = twilightCivilTime

    }


}