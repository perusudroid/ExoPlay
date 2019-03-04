package com.perusudroid.myapplication.common

object Constants {


    object BundleKeys {
        const val PLAY_WHEN_READY = "PLAY_WHEN_READY"
        const val KEY_WINDOW = "KEY_WINDOW"
        const val KEY_POSITION = "KEY_POSITION"
        const val PLAYABLE_POSITION = "PLAYABLE_POSITION"
        const val DATA = "DATA"
    }

    object Common{
        const val HANDLER_MSS = 1000L
        const val STATE_PLAYING = 1
        const val STATE_NOT_PLAYING = 2
    }

    object DB{
        const val DATABASE_NAME = "exo_db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "exoplay"
        const val COLUMN_ID = "id"
        const val COLUMN_VID_ID = "vid_id"
        const val COLUMN_POSITION = "position"
        const val COLUMN_STATE = "state"
        const val COLUMN_CREATED_TIMESTAMP = "created_on"
        const val COLUMN_UPDATED_TIMESTAMP = "updated_on"
    }
}