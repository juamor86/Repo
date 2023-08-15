package com.example.msspa_megusta_library.domain.usecases

import com.example.msspa_megusta_library.data.factory.LikeItRepositoryFactory
import com.example.msspa_megusta_library.domain.Strategy
import com.example.msspa_megusta_library.domain.base.SingleUseCase
import com.example.msspa_megusta_library.domain.entity.LikeItEntity
import com.example.msspa_megusta_library.domain.mappers.LikeItMapper
import io.reactivex.Single

class GetEventsUseCase(private val factory: LikeItRepositoryFactory) :
    SingleUseCase<LikeItEntity>() {

    override fun buildUseCase(): Single<LikeItEntity> =
        factory.create(Strategy.NETWORK).getEvents().map { LikeItMapper.convert(it) }

}