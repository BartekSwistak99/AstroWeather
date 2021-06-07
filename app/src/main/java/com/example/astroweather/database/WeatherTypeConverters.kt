package com.example.astroweather.database

import androidx.room.TypeConverter
import com.example.astroweather.Weather
import com.example.astroweather.database.forecast.SingleForecast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class WeatherTypeConverters {
    @TypeConverter
    fun fromWeatherList(weathers: List<Weather?>?): String? {
        if (weathers == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Weather?>?>() {}.type
        return gson.toJson(weathers, type)
    }

    @TypeConverter
    fun toWeatherList(countryLangString: String?): List<Weather>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Weather?>?>() {}.type
        return gson.fromJson<List<Weather>>(countryLangString, type)
    }

    @TypeConverter
    fun fromSingleForecastList(forecasts: List<SingleForecast?>?): String? {
        if (forecasts == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<SingleForecast?>?>() {}.type
        return gson.toJson(forecasts, type)
    }

    @TypeConverter
    fun toSingleForecastList(forecastString: String?): List<SingleForecast>? {
        if (forecastString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<SingleForecast?>?>() {}.type
        return gson.fromJson<List<SingleForecast>>(forecastString, type)
    }
}