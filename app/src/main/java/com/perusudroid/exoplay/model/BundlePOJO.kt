package com.perusudroid.exoplay.model

import java.io.Serializable

data class BundlePOJO (val mList : List<VideoListResponse>,
                       val playablePosition : Int,
                       val currentTitle : String,
                       val currentDesc : String
                       ) : Serializable