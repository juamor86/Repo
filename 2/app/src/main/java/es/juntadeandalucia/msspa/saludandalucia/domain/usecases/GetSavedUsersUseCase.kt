package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.ParamsSingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizUserEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.QuizUserMapper
import javax.crypto.Cipher

class GetSavedUsersUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    ParamsSingleUseCase<List<QuizUserEntity>, GetSavedUsersUseCase.Param>() {

    override fun buildUseCase(param: Param) =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getSavedUsers(
            param.cipherDecrypt
        ).map {
            QuizUserMapper.convert(it)
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
