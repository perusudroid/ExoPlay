package com.perusudroid.exoplay.model

import java.io.Serializable

data class VideoListResponse(var thumb: String? = null,
                             var description: String? = null,
                             var id: Int? = null,
                             var title: String? = null,
                             var url: String? = null) : Serializable