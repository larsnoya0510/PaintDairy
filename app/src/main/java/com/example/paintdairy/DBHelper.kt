package com.example.paintdairy

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.paintdairy.dataclass.Draws
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper

class DBHelper(var context: Context) :
    ManagedSQLiteOpenHelper(context,"paintDiary.db", null, 1) {
    var tableList: List<String> = listOf("Draws")
    companion object {
        private val TAG = "paintDiaryDB"
        private var instance: DBHelper?=null
        @Synchronized
        fun getInstance(ctx: Context, version: Int = 0): DBHelper {
            if (instance == null) {
                instance =DBHelper(ctx.applicationContext)
            }
            Log.d("TAG", "Instance RETURN")
            return instance!!
        }
    }
    override fun onCreate(db: SQLiteDatabase) {
        Log.d("TAG", "onCreate")
        DBInital(tableList,db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d("TAG", "onUpgrade")
    }
    fun DBInital(listTable : List<String>, db: SQLiteDatabase){
        Log.d("TAG", "Inital start")
        //var db: SQLiteDatabase
        for(i : Int in 0..listTable.count()-1){
            when(listTable[i]){
                "Draws"->{
                    var create_sql :String = """
                        CREATE TABLE IF NOT EXISTS "Draws" ( "DrawId" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "DrawPath"	TEXT NOT NULL, "Date" TEXT NOT NULL);
                    """
                    Log.d("TAG", "create_sql:" + create_sql)
                    db.execSQL(create_sql)
                    Log.d("TAG", "create Draws table Done")
                }
            }
        }
    }
    //query
    fun queryDrawsByDate(condition: String?): List<Draws> {
        Log.d("TAG", "start query")
        var m_conditionJudge : String = condition ?: "1=1"
        val sql = """
            select Draws.DrawId,Draws.DrawPath,Draws.Date from Draws where $m_conditionJudge
        """
        Log.d("TAG", "query sql: " + sql)
        var drawsArray = mutableListOf<Draws>()
        use {
            Log.d("TAG", "start get query")
            val cursor = rawQuery(sql, null)
            Log.d("TAG", "cursor: "+cursor.count)
            if(cursor.count>0) {
                if (cursor.moveToFirst()) {
                    while (true) {
                        var mDrawId = cursor.getLong(cursor.getColumnIndexOrThrow("DrawId"))
                        var mDrawPath = cursor.getString(cursor.getColumnIndexOrThrow("DrawPath"))
                        var mDate = cursor.getString(cursor.getColumnIndexOrThrow("Date"))
                        val info = Draws(mDrawId,mDrawPath,mDate)
                        drawsArray.add(info)
                        if (cursor.isLast) {
                            break
                        }
                        cursor.moveToNext()
                    }
                }
            }
            cursor.close()
        }
        return drawsArray
    }
    fun queryDateHasDraw(condition: String?): List<String> {
        Log.d("TAG", "start query")
        var m_conditionJudge : String = condition ?: "1=1"
        val sql = """
            select Draws.Date from Draws where $m_conditionJudge group by Draws.Date 
        """
        Log.d("TAG", "query sql: " + sql)
        var dateArray = mutableListOf<String>()
        use {
            Log.d("TAG", "start get query")
            val cursor = rawQuery(sql, null)
            Log.d("TAG", "cursor: "+cursor.count)
            if(cursor.count>0) {
                if (cursor.moveToFirst()) {
                    while (true) {
                        var mDate = cursor.getString(cursor.getColumnIndexOrThrow("Date"))
                        val info = mDate
                        dateArray.add(info)
                        if (cursor.isLast) {
                            break
                        }
                        cursor.moveToNext()
                    }
                }
            }
            cursor.close()
        }
        return dateArray
    }
    //insert
    fun insert(infoArray: MutableList<Draws>): Long {
        Log.d("TAG", "start insert")
        var result: Long = -1
        for (i in infoArray.indices) {
            val info = infoArray[i]
            //var tempArray: List<bookmarkInfo>
            val cv = ContentValues()
            cv.put("DrawPath", info.drawPath)
            cv.put("Date", info.date)
            use {
                result = insert("Draws", "", cv)
            }
            if (result == -1L) {
                return result
            }
        }
        return result
    }
    fun delete( condition: String): Int {
        Log.d("TAG", "start delete")
        var count = 0
        use {
            count = delete("Draws", condition, null)
        }
        return count
    }
}