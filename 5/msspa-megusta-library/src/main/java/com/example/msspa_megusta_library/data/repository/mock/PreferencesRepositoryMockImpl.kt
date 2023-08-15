package com.example.msspa_megusta_library.data.repository.mock

import com.example.msspa_megusta_library.data.entities.LikeItData
import com.example.msspa_megusta_library.domain.repository.PreferencesRepository
import io.reactivex.Completable
import io.reactivex.Single

class PreferencesRepositoryMockImpl : PreferencesRepository {
    override fun getSavedLikeIt(): Single<LikeItData> = Single.just(null)
    override fun saveLikeIt(likeItData: LikeItData): Completable = Completable.complete()
}