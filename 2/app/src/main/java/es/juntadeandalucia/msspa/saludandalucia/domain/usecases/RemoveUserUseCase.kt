package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizUserData
import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.ParamsSingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizUserEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.QuizUserMapper
import es.juntadeandalucia.msspa.saludandalucia.security.CrytographyManager

class RemoveUserUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    ParamsSingleUseCase<Boolean, RemoveUserUseCase.Param>() {

    override fun buildUseCase(customParams: Param) =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).removeUser(
            customParams.user, customParams.cryptoManager
        )

    class Param private constructor(
        val user: QuizUserData,
        val cryptoManager: CrytographyManager?
    ) {

        companion object {
            fun forRemoveUser(user: QuizUserEntity, cryptoManager: CrytographyManager?): Param {
                return Param(QuizUserMapper.convert(user), cryptoManager)
            }
        }
    }
}
