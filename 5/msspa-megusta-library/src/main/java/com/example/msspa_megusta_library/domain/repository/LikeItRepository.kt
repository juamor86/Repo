package com.example.msspa_megusta_library.domain.repository

import com.example.msspa_megusta_library.data.entities.ServiceData
import io.reactivex.Single

interface LikeItRepository {

    fun getEvents(): Single<ServiceData>

}