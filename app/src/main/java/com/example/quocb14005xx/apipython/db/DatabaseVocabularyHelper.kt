package com.example.quocb14005xx.apipython.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.quocb14005xx.apipython.model.HistoryVocabulary

class DatabaseVocabularyHelper(ctx: Context) : SQLiteOpenHelper(ctx, DBConstants.DB_NAME, null, DBConstants.VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(DBConstants.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(DBConstants.SQL_DELETE_TABLE)
        onCreate(db)
    }

    fun insert(itemHistoryVocabulary: HistoryVocabulary): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(DBConstants.COL_BASE64STR, itemHistoryVocabulary.base64str)
        values.put(DBConstants.COL_CONTENT, itemHistoryVocabulary.content)
        values.put(DBConstants.COL_DATE, itemHistoryVocabulary.date)
        val newRowID = db.insert(DBConstants.TABLE_NAME, null, values)
        return true
    }
    fun delete(itemId: Int):Boolean{
        val db = writableDatabase
        val selection = DBConstants.COL_ID + "= $itemId"
        return db.delete(DBConstants.TABLE_NAME,selection,null)>0
    }
    fun queryALlHistory(): ArrayList<HistoryVocabulary> {
        val items = ArrayList<HistoryVocabulary>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor= db.rawQuery("select * from ${DBConstants.TABLE_NAME}",null)
        } catch (e: Throwable) {
            db.execSQL(DBConstants.SQL_CREATE_TABLE)
            return ArrayList()
        }

        var base64: String
        var content: String
        var date: String
        if (cursor!!.moveToFirst()){
            while (cursor.isAfterLast==false){

                base64=cursor.getString(cursor.getColumnIndex(DBConstants.COL_BASE64STR))
                content=cursor.getString(cursor.getColumnIndex(DBConstants.COL_CONTENT))
                date=cursor.getString(cursor.getColumnIndex(DBConstants.COL_DATE))
                items.add(HistoryVocabulary(base64,content,date))
                cursor.moveToNext()
            }
        }
        return items
    }
}