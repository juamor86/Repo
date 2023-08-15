package com.example.msspa_megusta_library.di.module

import android.content.Context
import android.content.SharedPreferences
import com.example.msspa_megusta_library.EventsManager
import com.example.msspa_megusta_library.data.api.MeGustaApi
import com.example.msspa_megusta_library.data.factory.LikeItRepositoryFactory
import com.example.msspa_megusta_library.data.factory.PreferenceRepositoryFactory
import com.example.msspa_megusta_library.data.repository.mock.LikeItRepositoryMockImpl
import com.example.msspa_megusta_library.data.repository.mock.PreferencesRepositoryMockImpl
import com.example.msspa_megusta_library.data.repository.network.LikeItRepositoryNetworkImpl
import com.example.msspa_megusta_library.data.repository.persistence.PreferencesRepositoryPreferencesImpl
import com.example.msspa_megusta_library.domain.usecases.GetEventsUseCase
import com.example.msspa_megusta_library.domain.usecases.GetSavedLikeItUseCase
import com.example.msspa_megusta_library.domain.usecases.SaveLikeItUseCase
import com.example.msspa_megusta_library.utils.ApiConstants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LibraryModule(private val context: Context) {

    @Provides
    @Singleton
    fun providePreferences(): SharedPreferences = context.getSharedPreferences(
        ApiConstants.Preferences.PREF_NAME,
        Context.MODE_PRIVATE
    )

    @Provides
    @Singleton
    fun provideContext(): Context = context

    /*@Provides
    @Singleton
    fun provideMsspaAuthenticationConfig(): MsspaAuthenticationConfig = activity.authConfig*/

    @Provides
    fun provideValidateTokenUseCase(
        preferencesRepositoryFactory: LikeItRepositoryFactory
    ): GetEventsUseCase = GetEventsUseCase(
        preferencesRepositoryFactory
    )

    @Provides
    fun provideSaveLikeItTokenUseCase(
        preferencesRepositoryFactory: PreferenceRepositoryFactory
    ): SaveLikeItUseCase = SaveLikeItUseCase(
        preferencesRepositoryFactory
    )

    @Provides
    fun provideGetSaveLikeItTokenUseCase(
        preferencesRepositoryFactory: PreferenceRepositoryFactory
    ): GetSavedLikeItUseCase = GetSavedLikeItUseCase(
        preferencesRepositoryFactory
    )

    @Provides
    fun provideMsspaTokenManager(
        getEventsUseCase: GetEventsUseCase,
        saveLikeItUseCase: SaveLikeItUseCase,
        getSavedLikeItUseCase: GetSavedLikeItUseCase
    ): EventsManager =
        EventsManager(
            getEventsUseCase,
            saveLikeItUseCase,
            getSavedLikeItUseCase
        )

    @Provides
    fun provideLikeItFactory(
        meGustaApi: MeGustaApi
    ): LikeItRepositoryFactory =
        LikeItRepositoryFactory(
            LikeItRepositoryMockImpl(),
            LikeItRepositoryNetworkImpl(meGustaApi)
        )

    @Provides
    fun providePreferenceFactory(
        sharedPreferences: SharedPreferences
    ): PreferenceRepositoryFactory =
        PreferenceRepositoryFactory(
            PreferencesRepositoryPreferencesImpl(sharedPreferences),
            PreferencesRepositoryMockImpl()
        )


}