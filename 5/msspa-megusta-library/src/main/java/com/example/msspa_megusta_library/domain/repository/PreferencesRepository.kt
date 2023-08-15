package com.example.msspa_megusta_library.domain.repository

import com.example.msspa_megusta_library.data.entities.LikeItData
import io.reactivex.Completable
import io.reactivex.Single

interface PreferencesRepository {
    fun getSavedLikeIt(): Single<LikeItData>
    fun saveLikeIt(likeItData: LikeItData): Completable
}