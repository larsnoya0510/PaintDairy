package com.example.paintdairy

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.paintdairy.viewmodel.GetDateHasDrawViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var SqlConnect : DBHelper
    lateinit var mGetDateHasDrawViewModel : GetDateHasDrawViewModel
    private lateinit var mGetDateHasDrawObserve: Observer<MutableList<String>>
    var dates = ArrayList<CalendarDay>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SqlConnect=DBHelper.getInstance(this.applicationContext)
        var mEventDecorator = EventDecorator(Color.RED,dates)
        mGetDateHasDrawViewModel = ViewModelProvider(this).get(GetDateHasDrawViewModel::class.java)
        mGetDateHasDrawViewModel.mDBHelper = SqlConnect
        mGetDateHasDrawObserve = Observer {
            if(it.size>0){
                dates.clear()
                calendarView.removeDecorator(mEventDecorator)
                calendarView.invalidateDecorators()
                for(i in 0 until  it.size) {
                    var year = it[i].substring(0,4).toInt()
                    var month = it[i].substring(4,6).toInt()
                    var day = it[i].substring(6,8).toInt()
                    dates.add(CalendarDay.from(year,month,day ))
                }
                calendarView.addDecorator(EventDecorator(Color.RED,dates))
            }
        }
        mGetDateHasDrawViewModel.getData().observe(this,mGetDateHasDrawObserve)

        mGetDateHasDrawViewModel.getDateHasDraw()

//        var dates = ArrayList<CalendarDay>()
//        dates.add(CalendarDay.from(2020,8,23))
//        var mEventDecorator = EventDecorator(Color.RED,dates)
//        calendarView.addDecorator(mEventDecorator)
        calendarView.setOnDateChangedListener { widget, date, selected ->
            println("ZZZZZ $date")
//            calendarView.removeDecorator(mEventDecorator)
//            calendarView.invalidateDecorators()
//            dates.clear()
//            dates.add(CalendarDay.from(2020,8,24))
//            dates.add(CalendarDay.from(2020,8,21))
//            calendarView.addDecorator(EventDecorator(Color.RED,dates))
        }
    }
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyyMMdd")
        return format.format(date)
    }

    fun currentTimeToLong(): Long {
        return System.currentTimeMillis()
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("yyyyMMdd")
        return df.parse(date).time
    }
}
