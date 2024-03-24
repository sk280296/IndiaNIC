package com.app.indianic.base

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    val message: String? = null,
    @SerializedName("status_code")
    var statusCode: Int? = null,
    val success: Boolean? = false,
    val error: String? = null
)