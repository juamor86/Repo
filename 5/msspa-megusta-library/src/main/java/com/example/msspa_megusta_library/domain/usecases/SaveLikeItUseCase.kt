package com.example.msspa_megusta_library.domain.usecases

import com.example.msspa_megusta_library.data.factory.PreferenceRepositoryFactory
import com.example.msspa_megusta_library.domain.Strategy
import com.example.msspa_megusta_library.domain.base.CompletableUseCase
import com.example.msspa_megusta_library.domain.entity.LikeItEntity
import com.example.msspa_megusta_library.domain.mappers.LikeItMapper
import io.reactivex.Completable

class SaveLikeItUseCase(private val preferenceFactory: PreferenceRepositoryFactory) : CompletableUseCase() {

    private lateinit var likeEntity: LikeItEntity

    override fun buildUseCase(): Completable =
        preferenceFactory.create(Strategy.PREFERENCES).run {
            saveLikeIt(LikeItMapper.convert(likeEntity))
        }

    fun params(likeEntity: LikeItEntity): SaveLikeItUseCase =
        this@SaveLikeItUseCase.apply {
            this.likeEntity = likeEntity
        }

}