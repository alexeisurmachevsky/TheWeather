package com.example.theweather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel :ViewModel(){
    var cityName = MutableLiveData<String>()

}