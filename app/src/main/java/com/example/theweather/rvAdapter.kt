package com.example.theweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.theweather.models.ListInside
import com.example.theweather.models.fdf
import com.example.theweather.models.twf

internal class rvAdapter(val wlList:List<fdf>):RecyclerView.Adapter<WeatherHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder {
        val ll = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent,false)
        return WeatherHolder(ll)
    }

    override fun getItemCount(): Int {
        return 8
    }

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        val item = wlList[position]
        holder.itemView.findViewById<ImageView>(R.id.imageView).setImageResource(
            when (item.weather[0].main) {
                "Clear" -> R.drawable.ic_clear_day
                "Clouds" -> R.drawable.ic_few_clouds
                "Rain" -> R.drawable.ic_shower_rain
                "Thunderstorm"-> R.drawable.ic_storm_weather
                "Snow"-> R.drawable.ic_snow_weather
                else -> R.drawable.ic_unknown
            }
        )
        holder.itemView.findViewById<TextView>(R.id.dayTemp).text = (item.main.temp_max-273).toInt().toString()
        holder.itemView.findViewById<TextView>(R.id.nightTemp).text = (item.main.temp_min-273).toInt().toString()
        val timeText =
            Regex("\\d{4}-\\d{2}-\\d{2} (\\d{2}:\\d{2}):\\d{2}")
            .find(item.dt_txt)
            ?.groupValues?.get(1)

        holder.itemView.findViewById<TextView>(R.id.time).text = timeText
    }
}

class WeatherHolder(item: View):ViewHolder(item)
{

}