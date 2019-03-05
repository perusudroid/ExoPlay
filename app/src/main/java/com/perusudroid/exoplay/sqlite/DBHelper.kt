package com.perusudroid.exoplay.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.perusudroid.exoplay.common.Constants
import com.perusudroid.exoplay.common.Snippet
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

    /**
     * A video can be played or replayed. If it was played for first time, it will be added.
     * If it was replayed, position will be updated for that video
     */

    fun addOrUpdateOnConflict(vid_id: Int, pos: Long, state: Int) {

        if (pos == 0L) {
            return
        }

        val db = writableDatabase

        val values = ContentValues()

        values.put(Constants.DB.COLUMN_POSITION, pos)
        values.put(Constants.DB.COLUMN_STATE, state)
        values.put(Constants.DB.COLUMN_UPDATED_TIMESTAMP, Snippet.getCurrentDate())


        Log.d("DbHelper", "vid_id $vid_id isVidExists ${isVidExists(vid_id)} Pos ${getVidPosition(vid_id)}")

        if (isVidExists(vid_id)) {

            db.update(Constants.DB.TABLE_NAME, values, Constants.DB.COLUMN_VID_ID + " = ?", arrayOf(vid_id.toString()))

        } else {
            values.put(Constants.DB.COLUMN_VID_ID, vid_id)

            db.insert(Constants.DB.TABLE_NAME, null, values)
        }

        db.close()


        //printAllFromDB()

    }


    /**
     * Check video exists locally or not
     */

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



    /**
     * Check current video is exists and its state in PLAY_MODE
     */

    fun isPlayableVidExists(vid_id: Int): Boolean {

        val db = writableDatabase

        val sql = "SELECT " + Constants.DB.COLUMN_STATE + " FROM " + Constants.DB.TABLE_NAME + " WHERE " + Constants.DB.COLUMN_VID_ID + "=?"


        val cursor = db.rawQuery(
                sql,
                arrayOf(vid_id.toString())
        )

        if (cursor.count > 0) {
            cursor.moveToFirst()
            val tmp = cursor.getInt(cursor.getColumnIndex(Constants.DB.COLUMN_STATE))
            cursor.close()
            return tmp == Constants.Common.STATE_PLAYING
        } else {

            return false
        }

    }


    /**
     * Get the previous position of the video from DB
     */

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


    fun printAllFromDB() {

        val vidList = ArrayList<ExoPojo>()

        // Select All Query
        val selectQuery = "SELECT  * FROM " + Constants.DB.TABLE_NAME

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val mList = ExoPojo(cursor.getInt(cursor.getColumnIndex(Constants.DB.COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(Constants.DB.COLUMN_VID_ID)),
                        cursor.getLong(cursor.getColumnIndex(Constants.DB.COLUMN_POSITION)),
                        cursor.getInt(cursor.getColumnIndex(Constants.DB.COLUMN_STATE)),
                        cursor.getString(cursor.getColumnIndex(Constants.DB.COLUMN_UPDATED_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndex(Constants.DB.COLUMN_CREATED_TIMESTAMP))
                )

                vidList.add(mList)
            } while (cursor.moveToNext())
        }

        for (i in 0 until vidList.size) {
            Log.d("DBHelperX", "i $i id ${vidList[i].id} vid id ${vidList[i].vid_id} pos ${vidList[i].position} state ${vidList[i].state} updated ${vidList[i].updated_on} created ${vidList[i].created_on}")
        }

        // close db connection
        db.close()
        cursor.close()

    }



}