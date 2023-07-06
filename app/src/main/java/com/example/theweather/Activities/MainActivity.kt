package com.example.theweather.Activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theweather.Common.Common
import com.example.theweather.R
import com.example.theweather.RetrofitServieces
import com.example.theweather.databinding.ActivityMainBinding
import com.example.theweather.models.WModel
import com.example.theweather.models.twf
import com.example.theweather.models.tws
import com.example.theweather.rvAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var mService: RetrofitServieces
    lateinit var binding: ActivityMainBinding


    var lat: Double = 0.0
    var lon: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )

        mService = Common.retrofitService
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
       // getLocation()


        binding.rvWeather.layoutManager = LinearLayoutManager(applicationContext,RecyclerView.HORIZONTAL,false)
        binding.currentLocation.setOnClickListener {
            getLocation()
        }
    }

    private val activity = this
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100
            )
            return
        }


        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            lat = it.latitude
            lon = it.longitude
            getCurrentWeather("weather?lat=${lat}&lon=${lon}&appid=02df2d182c5922677742982922a5c7ee")
            get2WeekForecast("forecast?lat=${lat}&lon=${lon}&appid=02df2d182c5922677742982922a5c7ee")

        }

    }

    private fun getCurrentWeather(url:String) {
        mService.getCurrentWeather(url)
            .enqueue(object : Callback<WModel> {
                override fun onResponse(call: Call<WModel>, response: Response<WModel>) {
                    val wModel = response.body() as WModel
                    binding.wmodel = wModel
                    val weather = wModel.weather[0].main
                    binding.ivCurrentWeather.setImageResource(
                        when (weather) {
                            "Clear" -> R.drawable.ic_clear_day
                            "Clouds" -> R.drawable.ic_few_clouds
                            "Rain" -> R.drawable.ic_shower_rain
                            "Thunderstorm"-> R.drawable.ic_storm_weather
                            "Snow"-> R.drawable.ic_snow_weather
                            else -> R.drawable.ic_unknown
                        }
                    )

                }

                override fun onFailure(call: Call<WModel>, t: Throwable) {
                    binding.tvCurrTemp.text = "100"
                    Log.e("FailureGETWeather", t.message.toString())
                }

            })
    }
    private fun get2WeekForecast(url:String){
        mService.get2WeekForecast(url)
            .enqueue(object:Callback<tws>{
                override fun onResponse(
                    call: Call<tws>,
                    response: Response<tws>
                ) {

                    val resp = response.body() as tws
                    binding.rvWeather.adapter = rvAdapter(resp.list)
                }

                override fun onFailure(call: Call<tws>, t: Throwable) {
                    Log.e("FailureGETWeather", t.message.toString())
                }


            })
    }

}