package com.app.indianic.utils

import com.app.indianic.base.BaseResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Response


fun <RequestType> networkBoundResourceWithoutDb(
    fetch: suspend () -> Response<RequestType>
) = flow {

    emit(Resource.Loading(null))
    try {
        val response = fetch.invoke()

        if (response.isSuccessful) {
            emit(Resource.Success(response.body()))
        } else {
            val gson = Gson()
            val type = object : TypeToken<BaseResponse>() {}.type
            val errorResponse: BaseResponse? =
                gson.fromJson(response.errorBody()!!.charStream(), type)
            errorResponse?.statusCode = response.code()
            this.logError("Error Body: " + Gson().toJson(errorResponse))
            emit(Resource.Error(null, errorResponse))
        }
    } catch (e: Throwable) {
        //emit(Resource.Error(e,null))
        this.logError(e.message.toString())
        emit(Resource.Error(null, BaseResponse(error = e.message)))
    }


}