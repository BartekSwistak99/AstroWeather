package com.example.astroweather.fragments

import android.content.res.Configuration.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.astroweather.ForecastRecycler
import com.example.astroweather.ForecastViewHolder
import com.example.astroweather.InternetConnection.Companion.isOnline
import com.example.astroweather.InternetConnection.Companion.loadImage
import com.example.astroweather.MarginItemDecoration
import com.example.astroweather.R
import com.example.astroweather.database.forecast.ForecastTable
import com.example.astroweather.database.forecast.SingleForecast
import java.lang.Thread.sleep
import java.util.*
import kotlin.concurrent.thread


class ForecastFragment : Fragment(R.layout.fragment_forecast) {


    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
    }

    fun updateForecastInfo(forecastTable: ForecastTable, isCelsius: Boolean) {

        val forecastList: MutableList<SingleForecast> = emptyList<SingleForecast>().toMutableList()

        forecastTable.list.forEach {
            var calendar = Calendar.getInstance()
            calendar.timeInMillis = it.dt * 1000
            Log.i("recycler",calendar.get(Calendar.HOUR_OF_DAY).toString())
            if (calendar.get(Calendar.HOUR_OF_DAY) in 13..15) {
                forecastList.add(it)
            }
        }


        val recyclerView: RecyclerView = view!!.findViewById(R.id.forecast_recycler)
//        activity.i

        if(resources.configuration.orientation == ORIENTATION_LANDSCAPE && (resources.configuration.screenLayout and SCREENLAYOUT_SIZE_MASK) >= SCREENLAYOUT_SIZE_LARGE)
        {
            recyclerView.layoutManager = GridLayoutManager(activity, 2)
            recyclerView.addItemDecoration(MarginItemDecoration(18))
            Log.i("recycler","test")
            if(forecastList.size % 2 == 1)
                forecastList.removeAt(forecastList.size - 1)
        }
        else{
            recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        }
        recyclerView.adapter = ForecastRecycler(forecastList, isCelsius)
        updateImages(forecastList)
    }

    fun updateImages(forecastList: MutableList<SingleForecast>) {
        thread {
            val recyclerView: RecyclerView = view!!.findViewById(R.id.forecast_recycler)
            var flags = BooleanArray(recyclerView.adapter!!.itemCount)
            flags.fill(false)
            do {
                if (activity != null && isOnline(activity!!)) {
                    for (i in 0..recyclerView.adapter!!.itemCount) {
                        val viewHolder: ForecastViewHolder? =
                            recyclerView.findViewHolderForAdapterPosition(i) as ForecastViewHolder?
                        if(viewHolder!=null){
                            viewHolder.getImg().loadImage(forecastList[i].weather[0].icon, activity)
                            flags[i] = true
                        }
                    }
                }
                sleep(1000)
            } while (flags.contains(false))
        }

    }
}