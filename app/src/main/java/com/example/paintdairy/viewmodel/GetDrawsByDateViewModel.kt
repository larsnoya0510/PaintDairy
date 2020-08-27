package com.example.paintdairy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paintdairy.DBHelper
import com.example.paintdairy.dataclass.Draws

class GetDrawsByDateViewModel() : ViewModel() {
    var mDBHelper: DBHelper? = null
    private var getDrawByDateData = MutableLiveData<MutableList<Draws>>()

    fun getDrawsByDate( date:String) {
        var drawsList =mDBHelper?.queryDrawsByDate(""" Date = "${date}" """)?.toMutableList()
        getDrawByDateData.value = drawsList
    }

    fun getData(): MutableLiveData<MutableList<Draws>> {
        return getDrawByDateData
    }

    fun clearData() {
        getDrawByDateData.value = mutableListOf<Draws>()
    }
}