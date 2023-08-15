package com.example.msspa_megusta_library.data.factory

import com.example.msspa_megusta_library.data.factory.base.BaseRepositoryFactory
import com.example.msspa_megusta_library.data.repository.mock.PreferencesRepositoryMockImpl
import com.example.msspa_megusta_library.data.repository.persistence.PreferencesRepositoryPreferencesImpl
import com.example.msspa_megusta_library.domain.Strategy
import com.example.msspa_megusta_library.domain.repository.PreferencesRepository

class PreferenceRepositoryFactory(
    private val preferencesRepositoryPreferencesImpl: PreferencesRepositoryPreferencesImpl,
    private val preferencesRepositoryMockImpl: PreferencesRepositoryMockImpl
) :
    BaseRepositoryFactory<PreferencesRepository>() {
    override fun create(strategy: Strategy): PreferencesRepository =
        when (strategy) {
            Strategy.PREFERENCES -> preferencesRepositoryPreferencesImpl
            else -> preferencesRepositoryMockImpl
        }

}