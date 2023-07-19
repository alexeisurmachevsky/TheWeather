package com.example.theweather

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient

class Location(private val applicationContext: Context,
               private val fusedLocationProviderClient: FusedLocationProviderClient,
               private val activity: Activity) {

     fun getLocation(callback: (Double, Double) -> Unit) {

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
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100
            )
        }

        val location = fusedLocationProviderClient.lastLocation

        location.addOnSuccessListener {
                callback(it.latitude, it.longitude)
             }

    }
}