package com.nitishsharma.frndcal.main.home.repository

import com.nitishsharma.frndcal.main.home.datamodel.StoreTaskRequestModel
import com.nitishsharma.frndcal.main.utils.Result

interface HomeFragmentRepository {
    suspend fun storeTask(taskRequestModel: StoreTaskRequestModel): Result<Unit>
}