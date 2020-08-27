package com.example.paintdairy.customview

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.paintdairy.DBHelper
import com.example.paintdairy.dataclass.Draws
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.FileOutputStream

class CustomPanel : View {
    var points = ArrayList<PointF>()
    lateinit var vBitmap : Bitmap
    private lateinit var vBitmapCanvas : Canvas
    private lateinit var mpaint : Paint

    var drawColor : Int = Color.RED
    var drawStyle : Paint.Style =Paint.Style.STROKE
    var drawWidth :Float =10F
    var paintBackGroundColor = Color.WHITE
    constructor(context: Context?,attrs: AttributeSet) : super(context,attrs) {
        initDrawSetting()
        this.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT)
        this.post {
            vBitmap = Bitmap.createBitmap(this.width,this.height,Bitmap.Config.RGB_565)
            vBitmapCanvas = Canvas(vBitmap)
            vBitmapCanvas.drawColor(paintBackGroundColor)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 1 until points.size) {
            val p1 = points[i - 1]
            val p2 = points[i]
            if (Math.abs(p1.x - p2.x) < 50 && Math.abs(p1.y - p2.y) < 50) {
                canvas?.drawLine(p1.x, p1.y, p2.x, p2.y, mpaint)
                vBitmapCanvas.drawLine(p1.x, p1.y, p2.x, p2.y, mpaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event!=null) {
            for (i in 0 until event.getHistorySize()) {
                points.add(PointF(event?.getHistoricalX(i), event?.getHistoricalY(i)))
            }
            this.invalidate()
        }
        return true
    }
    //Reset
    fun resetCanvas() {
        points.clear()
        this.invalidate()
        vBitmapCanvas.drawColor(Color.WHITE)
    }
    //save as picture
    fun savePicture() {
        val SqlConnect= DBHelper.getInstance(this.context.applicationContext)
        (context as Activity).runOnUiThread {
            try {
                val time = LocalDateTime.now()
                val dateString = time.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                val timeString  = time.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                val path = context.getExternalFilesDir(null)!!.toString() + "/${dateString}-${timeString}.jpg"
                val fout = FileOutputStream(path)
                vBitmap.compress(Bitmap.CompressFormat.PNG, 100, fout)
                fout.close()
                //寫入到SQL
                var mDraws = Draws(null,"${dateString}-${timeString}.jpg",dateString)
                val mDrawsList = mutableListOf<Draws>(mDraws)
                var result=SqlConnect.insert(mDrawsList)
//                if(result==1L){
//                    println("ZZZZZ  Done")
//                }
//                else{
//                    println("ZZZZZ  Fail")
//                }
            } catch (x: Exception) {
                x.printStackTrace()
            }
        }
    }
    fun savePicture(mDate:String) {
        val SqlConnect= DBHelper.getInstance(this.context.applicationContext)
        (context as Activity).runOnUiThread {
            try {
                val time = LocalDateTime.now()
                val dateString = mDate
                val timeString  = time.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                val path = context.getExternalFilesDir(null)!!.toString() + "/${dateString}-${timeString}.jpg"
                val fout = FileOutputStream(path)
                vBitmap.compress(Bitmap.CompressFormat.PNG, 100, fout)
                fout.close()
                //寫入到SQL
                var mDraws = Draws(null,"${dateString}-${timeString}.jpg",dateString)
                val mDrawsList = mutableListOf<Draws>(mDraws)
                var result=SqlConnect.insert(mDrawsList)
                when{
                    result>= 1 ->{
                        Toast.makeText(this.context,"新增成功",Toast.LENGTH_SHORT).show()
                    }
                    else ->{
                        Toast.makeText(this.context,"新增失敗",Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (x: Exception) {
                x.printStackTrace()
            }
        }
    }
    private fun initDrawSetting() {
         drawColor = Color.RED
         drawStyle  =Paint.Style.STROKE
         drawWidth  =10F
        mpaint = Paint()
        mpaint.color = drawColor
        mpaint.style = drawStyle
        mpaint.strokeWidth = drawWidth
    }
    fun setting(drawColor : Int,drawStyle :Paint.Style,drawWidth :Float){
        mpaint.color = drawColor
        mpaint.style = drawStyle
        mpaint.strokeWidth = drawWidth
    }

}