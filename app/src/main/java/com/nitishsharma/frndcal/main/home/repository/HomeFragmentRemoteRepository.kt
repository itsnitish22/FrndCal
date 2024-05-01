package com.nitishsharma.frndcal.main.home.repository

import com.nitishsharma.frndcal.main.home.datamodel.StoreTaskRequestModel
import com.nitishsharma.frndcal.main.utils.Result
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject

class HomeFragmentRemoteRepository @Inject constructor(
    retrofit: Retrofit
) {

    private val homeApiService = retrofit.create(HomeAPIService::class.java)

    suspend fun storeTask(taskRequestModel: StoreTaskRequestModel): Result<Unit> {
        return try {
            val response = homeApiService.storeTaskRemote(taskRequestModel)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    interface HomeAPIService {
        @POST("api/storeCalendarTask")
        suspend fun storeTaskRemote(@Body taskRequestModel: StoreTaskRequestModel): Unit
    }
}