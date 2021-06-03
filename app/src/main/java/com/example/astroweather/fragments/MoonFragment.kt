package com.example.astroweather.fragments

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.astroweather.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MoonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoonFragment : Fragment(R.layout.fragment_moon) {
    private var moonriseTime: String = ""
    private var moonsetTime: String = ""
    private var newMoon: String = ""
    private var fullMoon: String = ""
    private var age: String = ""

    private lateinit var moonriseTimeView: TextView
    private lateinit var moonsetTimeView: TextView
    private lateinit var newMoonView: TextView
    private lateinit var fullMoonView: TextView
    private lateinit var ageView: TextView


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        moonriseTimeView = view!!.findViewById(R.id.moonrise_time)
        moonsetTimeView = view!!.findViewById(R.id.moonset_time)
        newMoonView = view!!.findViewById(R.id.new_moon)
        fullMoonView = view!!.findViewById(R.id.full_moon)
        ageView = view!!.findViewById(R.id.moon_age)
        refreshDisplayedInfo()
    }

    fun updateMoonInfo(
        moonriseTime: String,
        moonsetTime: String,
        newMoon: String,
        fullMoon: String,
        age: String
    ) {
        this.moonriseTime = moonriseTime
        this.moonsetTime = moonsetTime
        this.newMoon = newMoon
        this.fullMoon = fullMoon
        this.age = age
    }

    fun refreshDisplayedInfo() {
        moonriseTimeView.text = moonriseTime
        moonsetTimeView.text = moonsetTime
        newMoonView.text = newMoon
        fullMoonView.text = fullMoon
        ageView.text = age
    }


}