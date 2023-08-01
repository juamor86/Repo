package es.juntadeandalucia.msspa.saludandalucia.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.saludandalucia.data.factory.CovidCertificateRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.di.scope.PerFragment
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.CheckCovidCertificateUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetCovidCertTrustListUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetWalletIsActiveUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SaveCovidCertificateUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SetWalletIsActiveUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.ValidateContraindicationUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.ValidateGreenPassUseCase

@Module
class CertificateModule {

    //region - Use cases
    @PerFragment
    @Provides
    fun provideValidateGreenPassUseCase(
        context: Context,
        getCovidCertTrustListUseCase: GetCovidCertTrustListUseCase
    ): ValidateGreenPassUseCase =
        ValidateGreenPassUseCase(context, getCovidCertTrustListUseCase)

    @PerFragment
    @Provides
    fun provideValidateContraindicationUseCase(
        context: Context,
        getCovidCertTrustListUseCase: GetCovidCertTrustListUseCase
    ) : ValidateContraindicationUseCase =
        ValidateContraindicationUseCase(context, getCovidCertTrustListUseCase)

    @PerFragment
    @Provides
    fun provideSaveCovidCertificateUseCase(covidCertificateRepository: CovidCertificateRepositoryFactory): SaveCovidCertificateUseCase =
        SaveCovidCertificateUseCase(
            covidCertificateRepository
        )

    @PerFragment
    @Provides
    fun provideGetTrustListUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory): GetCovidCertTrustListUseCase =
        GetCovidCertTrustListUseCase(preferencesRepositoryFactory)

    @PerFragment
    @Provides
    fun provideCheckCertificateUseCase(covidCertificateRepository: CovidCertificateRepositoryFactory) : CheckCovidCertificateUseCase =
        CheckCovidCertificateUseCase(covidCertificateRepository)

    @PerFragment
    @Provides
    fun provideGetWalletIsActiveUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory) : GetWalletIsActiveUseCase =
        GetWalletIsActiveUseCase(preferencesRepositoryFactory)

    @Provides
    fun provideSetWalletIsActiveUseCase(preferencesRepositoryFactory: PreferencesRepositoryFactory) : SetWalletIsActiveUseCase =
        SetWalletIsActiveUseCase(preferencesRepositoryFactory)

    //endregion
}
