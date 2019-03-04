package com.perusudroid.myapplication.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.perusudroid.myapplication.common.Constants
import java.util.*

class DBHelper(context: Context) :
        SQLiteOpenHelper(context, Constants.DB.DATABASE_NAME, null, Constants.DB.DATABASE_VERSION) {


    val CREATE_TABLE = (
            "CREATE TABLE " + Constants.DB.TABLE_NAME + "("
                    + Constants.DB.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Constants.DB.COLUMN_VID_ID + " INTEGER,"
                    + Constants.DB.COLUMN_POSITION + " INTEGER,"
                    + Constants.DB.COLUMN_STATE + " INTEGER,"
                    + Constants.DB.COLUMN_UPDATED_TIMESTAMP + " DATETIME,"
                    + Constants.DB.COLUMN_CREATED_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")")

    companion object {

        private var mInstance: DBHelper? = null

        fun getInstance(context: Context): DBHelper {
            if (mInstance == null) {
                mInstance = DBHelper(context)
            }
            return mInstance as DBHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + Constants.DB.TABLE_NAME)
        onCreate(db);
    }

    fun addOrUpdateOnConflict(vid_id: Int, pos: Long, state: Int) {

        if (pos == 0L) {
            return
        }

        val db = writableDatabase

        val values = ContentValues()


        Log.d("DbHelper", "isVidExists ${isVidExists(vid_id)} Pos ${getVidPosition(vid_id)}")

        if (isVidExists(vid_id)) {
            values.put(Constants.DB.COLUMN_POSITION, pos)
            values.put(Constants.DB.COLUMN_UPDATED_TIMESTAMP, Calendar.getInstance().getTime().toString())
            db.update(Constants.DB.TABLE_NAME, values, Constants.DB.COLUMN_VID_ID + " = ?", arrayOf(vid_id.toString()))
            Log.d("DbHelper", "Updated")
        } else {
            values.put(Constants.DB.COLUMN_VID_ID, vid_id)
            values.put(Constants.DB.COLUMN_POSITION, pos)
            values.put(Constants.DB.COLUMN_STATE, state)
            values.put(Constants.DB.COLUMN_UPDATED_TIMESTAMP, Calendar.getInstance().getTime().toString())
            db.insert(Constants.DB.TABLE_NAME, null, values)
            Log.d("DbHelper", "Inserted")
        }

        Log.d("Vix", "vid_id $vid_id pos $pos state $state")

        db.close()


        printAllFromDB()

    }

    fun getVidState(vid_id: Int): Int {

        val db = writableDatabase

        val cursor = db.rawQuery(
                "SELECT " + Constants.DB.COLUMN_STATE + " FROM " + Constants.DB.TABLE_NAME + " WHERE " + Constants.DB.COLUMN_VID_ID + "=?",
                arrayOf(vid_id.toString())
        )

        if (cursor!!.count > 0) {
            cursor.moveToFirst()
            val tmp = cursor.getInt(cursor.getColumnIndex(Constants.DB.COLUMN_STATE))
            cursor.close()
            return tmp
        } else {
            return Constants.Common.STATE_NOT_PLAYING
        }

    }


    fun getVidPosition(vid_id: Int): Long {

        val db = writableDatabase

        val cursor = db.rawQuery(
                "SELECT " + Constants.DB.COLUMN_POSITION + " FROM " + Constants.DB.TABLE_NAME + " WHERE " + Constants.DB.COLUMN_VID_ID + "=?",
                arrayOf(vid_id.toString())
        )

        if (cursor!!.count > 0) {
            cursor.moveToFirst()
            val tmp = cursor.getInt(cursor.getColumnIndex(Constants.DB.COLUMN_POSITION))
            cursor.close()
            return tmp.toLong()
        }

        return 0

    }

    //todo - need to check

    fun isPlayableVidExists(vid_id: Int): Boolean {

        val db = writableDatabase

        val sql = "SELECT " + Constants.DB.COLUMN_STATE + " FROM " + Constants.DB.TABLE_NAME + " WHERE " + Constants.DB.COLUMN_VID_ID + "=?"

        Log.d("DBHelper", "isPlayableVidExists sql -> $sql")

        val cursor = db.rawQuery(
                sql,
                arrayOf(vid_id.toString())
        )


        Log.d("DBHelper", "isPlayableVid ${cursor!!.count} vid_id $vid_id")

        if (cursor.count > 0) {
            cursor.moveToFirst()
            val tmp = cursor.getInt(cursor.getColumnIndex(Constants.DB.COLUMN_STATE))
            cursor.close()
            return tmp == 1
        } else {

            return false
        }

    }


    fun printAllFromDB(): List<ExoPojo> {

        val records = ArrayList<ExoPojo>()

        // Select All Query
        val selectQuery = "SELECT  * FROM " + Constants.DB.TABLE_NAME

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val note = ExoPojo(cursor.getInt(cursor.getColumnIndex(Constants.DB.COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(Constants.DB.COLUMN_VID_ID)),
                        cursor.getLong(cursor.getColumnIndex(Constants.DB.COLUMN_POSITION)),
                        cursor.getInt(cursor.getColumnIndex(Constants.DB.COLUMN_STATE)),
                        cursor.getString(cursor.getColumnIndex(Constants.DB.COLUMN_UPDATED_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndex(Constants.DB.COLUMN_CREATED_TIMESTAMP))
                )

                records.add(note)
            } while (cursor.moveToNext())
        }

        for (i in 0 until records.size) {
            Log.d("DBHelperX", "i $i id ${records[i].id} vid id ${records[i].vid_id} pos ${records[i].position} state ${records[i].state} updated ${records[i].updated_on} created ${records[i].created_on}")
        }

        // close db connection
        db.close()
        cursor.close()

        // return notes list
        return records
    }


    fun isVidExists(vid_id: Int): Boolean {

        val db = writableDatabase

        val cursor = db.rawQuery(
                "SELECT " + Constants.DB.COLUMN_POSITION + " FROM " + Constants.DB.TABLE_NAME + " WHERE " + Constants.DB.COLUMN_VID_ID + "=?",
                arrayOf(vid_id.toString())
        )
        val tmp = cursor!!.count
        cursor.close()
        return tmp > 0

    }

}