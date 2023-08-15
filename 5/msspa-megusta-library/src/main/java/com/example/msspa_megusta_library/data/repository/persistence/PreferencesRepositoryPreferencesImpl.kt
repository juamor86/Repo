package com.example.msspa_megusta_library.data.repository.persistence

import android.content.SharedPreferences
import com.example.msspa_megusta_library.data.entities.LikeItData
import com.example.msspa_megusta_library.domain.repository.PreferencesRepository
import com.example.msspa_megusta_library.utils.ApiConstants.Preferences.PREF_LIKE_IT
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.Single
import java.lang.reflect.Type

class PreferencesRepositoryPreferencesImpl(private val sharedPreferences: SharedPreferences) :
    PreferencesRepository {


    override fun getSavedLikeIt(): Single<LikeItData> =
        Single.create { single ->
            val savedInfo = sharedPreferences.getString(PREF_LIKE_IT, null)
            val type: Type = object : TypeToken<LikeItData>() {}.type

            savedInfo?.let { single.onSuccess(Gson().fromJson(savedInfo, type)) }
                ?: single.onError(Throwable())
        }

    override fun saveLikeIt(likeItData: LikeItData): Completable =
        Completable.create { completable ->

            val likeItJSON = Gson().toJson(likeItData)
            sharedPreferences.edit().putString(PREF_LIKE_IT, likeItJSON).apply()
            completable.onComplete()
        }


}