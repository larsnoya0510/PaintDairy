package com.example.paintdairy

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.paintdairy.viewmodel.GetDateHasDrawViewModel
import com.jakewharton.threetenabp.AndroidThreeTen
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import org.threeten.bp.DateTimeUtils.toLocalDate
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    lateinit var SqlConnect : DBHelper
    lateinit var mGetDateHasDrawViewModel : GetDateHasDrawViewModel
    private lateinit var mGetDateHasDrawObserve: Observer<MutableList<String>>
    lateinit var mEventDecorator : EventDecorator
    var dates = ArrayList<CalendarDay>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SqlConnect=DBHelper.getInstance(this.applicationContext)
        mEventDecorator = EventDecorator(Color.RED,dates)
        calendarView.addDecorator(TodayDecorator(CalendarDay.today(),this))
        mGetDateHasDrawViewModel = ViewModelProvider(this).get(GetDateHasDrawViewModel::class.java)
        mGetDateHasDrawViewModel.mDBHelper = SqlConnect
        mGetDateHasDrawObserve = Observer {
            dates.clear()
            calendarView.removeDecorator(mEventDecorator)
//            calendarView.invalidateDecorators()
            if(it.size>0){
                for(i in 0 until  it.size) {
                    val mLocalDate=convertStringToDate(it[i])
                    val year = mLocalDate.year
                    val month = mLocalDate.month.value
                    val day = mLocalDate.dayOfMonth
                    dates.add(CalendarDay.from(year,month,day ))
                }
                mEventDecorator = EventDecorator(Color.RED,dates)
                calendarView.addDecorator(mEventDecorator)

            }
            calendarView.invalidateDecorators()
        }
        mGetDateHasDrawViewModel.getData().observe(this,mGetDateHasDrawObserve)

        calendarView.setOnDateChangedListener { widget, date, selected ->
            println("ZZZZZ $date")
            val intent =Intent(this,DateActivity::class.java)
            val dateFormmat = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.getDefault())
            val mDate=date.date.format(dateFormmat)
            intent.putExtra("Date",mDate)
            startActivityForResult(intent, Companion.OPENDRAWACTIVITY)
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
    fun convertStringToDate(date: String): LocalDate {
        val df = SimpleDateFormat("yyyyMMdd")
        val dateTrans = Instant.ofEpochMilli(df.parse(date).time).atZone(ZoneId.systemDefault()).toLocalDate()
        val year = dateTrans.year
        val month = dateTrans.month.value
        val day = dateTrans.dayOfMonth
        return dateTrans
    }

    override fun onDestroy() {
        mGetDateHasDrawViewModel.getData().removeObserver(mGetDateHasDrawObserve)
        super.onDestroy()
    }

    companion object {
        const val OPENDRAWACTIVITY = 100
    }
}
