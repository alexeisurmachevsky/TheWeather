package com.example.theweather.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
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
    lateinit var viewModel: MainViewModel

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mService = Common.retrofitService


        binding.rvWeather.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewModel.getForecastByCurrentLocation(this, fusedLocationProviderClient, this, mService)

        viewModel.currentWModel.observe(this) {
            binding.wmodel = it


            val ivcw: Int
            val ml: Int
            val cv: Int
            if (viewModel.wModel == null) {
                ivcw = R.drawable.ic_unknown
                ml = R.drawable.unknown_bg
                cv = R.color.atmosphere
            } else {
                when (viewModel.wModel!!.weather[0].main) {
                    "Clear" -> {
                        ivcw = R.drawable.ic_clear_day
                        ml = R.drawable.clear_bg
                        cv = R.color.clear
                    }

                    "Clouds" -> {
                        ivcw = R.drawable.ic_few_clouds
                        ml = R.drawable.clouds_bg
                        cv = R.color.clouds

                    }

                    "Rain" -> {
                        ivcw = R.drawable.ic_shower_rain
                        ml = R.drawable.rain_bg
                        cv = R.color.rain

                    }

                    "Thunderstorm" -> {
                        ivcw = R.drawable.ic_storm_weather
                        ml = R.drawable.thunderstrom_bg
                        cv = R.color.thunderstorm

                    }

                    "Snow" -> {
                        ivcw = R.drawable.ic_snow_weather
                        ml = R.drawable.snow_bg
                        cv = R.color.snow

                    }

                    else -> {
                        ivcw = R.drawable.ic_unknown
                        ml = R.drawable.atmosphere_bg
                        cv = R.color.atmosphere

                    }
                }
            }
            binding.ivCurrentWeather.setImageResource(ivcw)
            binding.mainLayout.background = resources.getDrawable(ml)
            binding.cardView.setCardBackgroundColor(resources.getColor(cv))

        }

        viewModel.currentWList.observe(this) {
            if (it != null)
                binding.rvWeather.adapter = rvAdapter(it)

            binding.loading.animate().apply {
                duration = 300
                alpha(0f)
            }

        }


        binding.currentLocation.setOnClickListener {
            viewModel.getForecastByCurrentLocation(
                this,
                fusedLocationProviderClient,
                this,
                mService
            )
        }

        binding.searchCity.setOnClickListener {
            binding.searchCity.setOnKeyListener { _, i, keyEvent ->
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                    val cityName = binding.searchCity.text.toString()
                    viewModel.getForecastByCityName(mService, cityName)
                    return@setOnKeyListener true
                }
                false
            }
        }
    }


}