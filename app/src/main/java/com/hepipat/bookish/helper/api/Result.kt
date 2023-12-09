package com.hepipat.bookish.helper.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hepipat.bookish.core.data.remote.response.ErrorResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable) : Result<Nothing>
    object Loading : Result<Nothing>
}

suspend fun <T> proceed(coroutines: suspend () -> T): Result<T> {
    return try {
        Result.Success(coroutines.invoke())
    } catch (e: Exception) {
        if (e is HttpException) {
            val errorMessageResponseType = object : TypeToken<ErrorResponse>() {}.type
            val error: ErrorResponse = Gson().fromJson(e.response()?.errorBody()?.charStream(), errorMessageResponseType)
            if (!error.error.isNullOrEmpty()) {
                Result.Error(error)
            } else {
                Result.Error(e)
            }
        } else {
            Result.Error(e)
        }
    }
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}