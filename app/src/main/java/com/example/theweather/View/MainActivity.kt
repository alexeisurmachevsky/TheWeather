package com.example.theweather.View

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theweather.Data.Retrofit.Common
import com.example.theweather.Data.Retrofit.RetrofitServieces
import com.example.theweather.Data.rvAdapter
import com.example.theweather.R
import com.example.theweather.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity(), LifecycleOwner {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var mService: RetrofitServieces
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mService = Common.retrofitService

        binding.rvWeather.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        viewModel.currentWModel.observe(this, Observer {
            binding.wmodel = it
            binding.ivCurrentWeather.setImageResource(
                if (viewModel.wModel==null)
                    R.drawable.ic_unknown
                else{
                    when (viewModel.wModel!!.weather[0].main) {
                        "Clear" -> R.drawable.ic_clear_day
                        "Clouds" -> R.drawable.ic_few_clouds
                        "Rain" -> R.drawable.ic_shower_rain
                        "Thunderstorm"-> R.drawable.ic_storm_weather
                        "Snow"-> R.drawable.ic_snow_weather
                        else -> R.drawable.ic_unknown
                    }
                }
            )

        })

        viewModel.currentWList.observe(this, Observer {
            if(it != null)
                binding.rvWeather.adapter =  rvAdapter(it)
        })


        binding.currentLocation.setOnClickListener {
            viewModel.getForecastByCurrentLocation(this, fusedLocationProviderClient, this, mService)
        }

        binding.searchCity.setOnClickListener{
            binding.searchCity.setOnKeyListener { _, i, keyEvent ->
                if(i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP)
                {
                    val cityName = binding.searchCity.text.toString()
                    viewModel.getForecastByCityName( mService, cityName)
                    return@setOnKeyListener true
                }
                false
            }
        }
    }


}