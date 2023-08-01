package es.juntadeandalucia.msspa.authentication.domain.base

import es.juntadeandalucia.msspa.authentication.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.domain.mappers.UserMapper
import javax.crypto.Cipher

class GetSavedUsersUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    ParamsSingleUseCase<List<MsspaAuthenticationUserEntity>, GetSavedUsersUseCase.Param>() {

    private lateinit var msspaAuthenticationUser: MsspaAuthenticationUserEntity

    override fun buildUseCase(params: Param) =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getSavedUsers(
            params.cipherDecrypt
        ).map {
            UserMapper.convert(it)
        }

    class Param private constructor(
        val cipherDecrypt: Cipher
    ) {

        companion object {
            fun forSavedUsers(
                cipherDecrypt: Cipher
            ): Param {
                return Param(cipherDecrypt)
            }
        }
    }
}
