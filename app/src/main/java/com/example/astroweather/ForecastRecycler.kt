package com.example.astroweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.astroweather.InternetConnection.Companion.loadImage
import com.example.astroweather.database.forecast.SingleForecast
import com.example.astroweather.fragments.WeatherFragment.Companion.toCelsius
import com.example.astroweather.fragments.WeatherFragment.Companion.toFahrenheit
import java.util.*

class ForecastRecycler(private val forecastList: List<SingleForecast>, private val isCelsius: Boolean) :
    RecyclerView.Adapter<ForecastViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_list_item, parent, false)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bindItems(forecastList[position], isCelsius)

    }

    override fun getItemCount(): Int {
        return forecastList.size
    }
}

class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var singleForecast: SingleForecast? =null
    fun bindItems(singleForecast: SingleForecast, isCelsius: Boolean) {
        this.singleForecast=singleForecast
        val date: TextView = itemView.findViewById(R.id.date1)
        val temperature: TextView = itemView.findViewById(R.id.temperature1)
        val weather: TextView = itemView.findViewById(R.id.weather1)
        val pressure: TextView = itemView.findViewById(R.id.pressure1)
        val humidity: TextView = itemView.findViewById(R.id.humidity1)
        val visibility: TextView = itemView.findViewById(R.id.visibility1)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = singleForecast.dt *1000
        date.text = "%02d.%02d %02d:%02d".format(
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )
        temperature.text =
            if (isCelsius) "%.2f °C".format(toCelsius(singleForecast.main.temp)) else "%.2f °F".format(
                toFahrenheit(singleForecast.main.temp)
            )
        weather.text = singleForecast.weather[0].description
        pressure.text = singleForecast.main.pressure.toString() + " hpa"
        humidity.text = singleForecast.main.humidity.toString() + " %"
        visibility.text = singleForecast.visibility.toString() + " m"

    }
    fun getImg():ImageView{
        val weatherImg: ImageView = itemView.findViewById(R.id.weather_img1)
        return weatherImg
    }

}