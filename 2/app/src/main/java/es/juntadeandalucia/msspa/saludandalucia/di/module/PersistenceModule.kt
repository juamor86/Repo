package es.juntadeandalucia.msspa.saludandalucia.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.saludandalucia.data.persistence.AppDataBase
import es.juntadeandalucia.msspa.saludandalucia.data.persistence.CovidCertificateDao
import es.juntadeandalucia.msspa.saludandalucia.data.persistence.NotificationDao
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Provides
    @Singleton
    fun provideNotificationDao(appDataBase: AppDataBase): NotificationDao =
        appDataBase.notificationDao()

    @Provides
    @Singleton
    fun provideCovidCertificateDao(appDataBase: AppDataBase): CovidCertificateDao =
        appDataBase.covidCertificatesDao()

    @Provides
    @Singleton
    fun provideAppDataBase(context: Context): AppDataBase = AppDataBase.getInstance(context)
}
