package com.example.msspa_megusta_library.data.repository.network

import com.example.msspa_megusta_library.data.api.MeGustaApi
import com.example.msspa_megusta_library.data.entities.ServiceData
import com.example.msspa_megusta_library.domain.repository.LikeItRepository
import io.reactivex.Single

class LikeItRepositoryNetworkImpl(private val meGustaApi: MeGustaApi) : LikeItRepository {

    override fun getEvents(): Single<ServiceData> = meGustaApi.getLikeIt()


}