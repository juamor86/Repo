package com.example.msspa_megusta_library.data.repository.mock

import com.example.msspa_megusta_library.data.entities.ServiceData
import com.example.msspa_megusta_library.domain.repository.LikeItRepository
import io.reactivex.Single

class LikeItRepositoryMockImpl:LikeItRepository {
    override fun getEvents(): Single<ServiceData> {
        TODO("Not yet implemented")
    }
}