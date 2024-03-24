package com.app.indianic.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.indianic.response.WeatherForecastResponse
import com.app.indianic.base.BaseViewHolder
import com.app.indianic.databinding.ItemBusinessHomeBinding
import com.app.indianic.utils.AppUtil

class WeatherForecastAdapter(
    private val context: Context,
    private var list: List<WeatherForecastResponse.DayData>
) :
    RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val viewBinding: ItemBusinessHomeBinding = ItemBusinessHomeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder(val binding: ItemBusinessHomeBinding) :
        BaseViewHolder(binding) {

        override fun clear() {
        }

        override fun onBind(position: Int) {
            super.onBind(position)
            val data = list[position]
            binding?.apply {
                if (data.weather.isNotEmpty())
                    tvWeather.text = "Weather: ${data.weather[0].main.toString()}"
                tvTemp.text = "Temperature: ${data.main.temp.toString()}"
                tvPressure.text = "Pressure: ${data.main.pressure.toString()}"
                tvHumidity.text = "Humidity: ${data.main.humidity.toString()}"
                tvWindSpeed.text = "Wind Speed: ${data.wind.speed.toString()}"
                tvDate.text = "Date: ${
                    AppUtil.getDateFormatted(data.dt_txt, "yyyy-MM-dd HH:mm:ss", "dd MMM yyyy")
                }"
            }
        }


    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

}