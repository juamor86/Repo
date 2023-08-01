package es.juntadeandalucia.msspa.authentication.di.component

import dagger.Component
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationManager
import es.juntadeandalucia.msspa.authentication.MsspaTokenManager
import es.juntadeandalucia.msspa.authentication.data.factory.AuthorizeRepositoryFactory
import es.juntadeandalucia.msspa.authentication.data.factory.LoginPersonalDataRepositoryFactory
import es.juntadeandalucia.msspa.authentication.data.factory.api.MSSPAAuthorizeApi
import es.juntadeandalucia.msspa.authentication.data.factory.api.MSSPALoginApi
import es.juntadeandalucia.msspa.authentication.di.module.NetModule
import javax.inject.Singleton

@Singleton
@Component(modules = [NetModule::class])
interface NetComponent {

    val msspaLoginApi: MSSPALoginApi

    val msspaAuthorizeApi: MSSPAAuthorizeApi

    val authorizeRepositoryFactory: AuthorizeRepositoryFactory

    val loginPersonalDataRepositoryFactory: LoginPersonalDataRepositoryFactory

    val msspaTokenManager:MsspaTokenManager

    fun inject(msspaAuthenticationManager: MsspaAuthenticationManager)
}