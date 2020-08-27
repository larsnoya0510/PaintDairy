package com.example.paintdairy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paintdairy.DBHelper
import com.example.paintdairy.dataclass.Draws

class GetDateHasDrawViewModel() : ViewModel() {
    var mDBHelper:DBHelper? = null
    private var getDateHasDrawData = MutableLiveData<MutableList<String>>()

    fun getDateHasDraw() {
        var dateList =mDBHelper?.queryDateHasDraw(null)?.toMutableList()
//        dateList?.add("20200823")
        getDateHasDrawData.value = dateList
    }

    fun getData(): MutableLiveData<MutableList<String>> {
        return getDateHasDrawData
    }

    fun clearData() {
        getDateHasDrawData.value = mutableListOf<String>()
    }
}