package com.perusudroid.exoplay.common

import java.text.SimpleDateFormat
import java.util.*


object Snippet {



    fun getCurrentDate(): String? {

        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        return df.format(Calendar.getInstance().time)

    }

}