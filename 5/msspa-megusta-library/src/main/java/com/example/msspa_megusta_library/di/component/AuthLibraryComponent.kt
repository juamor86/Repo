package com.example.msspa_megusta_library.di.component

import android.content.Context
import android.content.SharedPreferences
import com.example.msspa_megusta_library.EventsManager
import com.example.msspa_megusta_library.MeGustaManager
import com.example.msspa_megusta_library.data.api.MeGustaApi
import com.example.msspa_megusta_library.data.factory.LikeItRepositoryFactory
import com.example.msspa_megusta_library.di.module.LibraryModule
import com.example.msspa_megusta_library.di.module.NetModule
import dagger.Component

import javax.inject.Singleton

@Singleton
@Component(modules = [LibraryModule::class, NetModule::class])
internal interface AuthLibraryComponent {

    val context: Context

    val msspaLoginApi: MeGustaApi

    val preferences: SharedPreferences

    val preferencesRepositoryFactory: LikeItRepositoryFactory

    val eventsManager: EventsManager

    fun inject(meGustaManager: MeGustaManager)

}