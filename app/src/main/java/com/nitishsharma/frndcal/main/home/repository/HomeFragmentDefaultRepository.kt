package com.nitishsharma.frndcal.main.home.repository

import com.nitishsharma.frndcal.main.home.datamodel.StoreTaskRequestModel
import com.nitishsharma.frndcal.main.utils.Result
import javax.inject.Inject

class HomeFragmentDefaultRepository @Inject constructor(val repository: HomeFragmentRemoteRepository) :
    HomeFragmentRepository {
    override suspend fun storeTask(taskRequestModel: StoreTaskRequestModel): Result<Unit> {
        return repository.storeTask(taskRequestModel)
    }
}