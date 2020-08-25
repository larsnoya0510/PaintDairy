package com.example.paintdairy

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.paintdairy.viewmodel.GetDateHasDrawViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var SqlConnect : DBHelper
    lateinit var mGetDateHasDrawViewModel : GetDateHasDrawViewModel
    private lateinit var mGetDateHasDrawObserve: Observer<MutableList<String>>
    var dates = ArrayList<CalendarDay>()
    @RequiresApi(Build.VERSION_CODES.O)
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
                    val mCal = convertStringToDate(it[i])
                    val year = mCal.get(Calendar.YEAR)
                    val month = mCal.get(Calendar.MONTH)+1
                    val day = mCal.get(Calendar.DAY_OF_MONTH)
                    dates.add(CalendarDay.from(year,month,day ))
                }
                calendarView.addDecorator(EventDecorator(Color.RED,dates))
            }
        }
        mGetDateHasDrawViewModel.getData().observe(this,mGetDateHasDrawObserve)

        calendarView.setOnDateChangedListener { widget, date, selected ->
            println("ZZZZZ $date")

        }
    }

    override fun onResume() {
        super.onResume()
        mGetDateHasDrawViewModel.getDateHasDraw()
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
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun convertStringToDate2(date: String): LocalDate {
//        val df = SimpleDateFormat("yyyyMMdd")
//        val dateTrans = LocalDate.parse(date,DateTimeFormatter.ofPattern("yyyyMMdd"))
//        val year = dateTrans.year
//        val month = dateTrans.month
//        val day = dateTrans.dayOfMonth
//        return dateTrans
//    }
    fun convertStringToDate(date: String): Calendar {
        val df = SimpleDateFormat("yyyyMMdd")
        val datetime= df.parse(date).time
        val mCal= Calendar.getInstance(Locale.TAIWAN)
        mCal.timeInMillis = datetime
//        val year = mCal.get(Calendar.YEAR)
//        val month = mCal.get(Calendar.MONTH)
//        val day = mCal.get(Calendar.DAY_OF_MONTH)
        return mCal
    }
}
