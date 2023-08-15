package com.example.msspa_megusta_library.domain.usecases

import com.example.msspa_megusta_library.data.factory.PreferenceRepositoryFactory
import com.example.msspa_megusta_library.domain.Strategy
import com.example.msspa_megusta_library.domain.base.SingleUseCase
import com.example.msspa_megusta_library.domain.entity.LikeItEntity
import com.example.msspa_megusta_library.domain.mappers.LikeItMapper
import io.reactivex.Single

class GetSavedLikeItUseCase(private val preferenceFactory: PreferenceRepositoryFactory) :
    SingleUseCase<LikeItEntity>() {

    override fun buildUseCase(): Single<LikeItEntity> =
        preferenceFactory.create(Strategy.PREFERENCES).run {
            getSavedLikeIt().map { LikeItMapper.convert(it) }
        }

}