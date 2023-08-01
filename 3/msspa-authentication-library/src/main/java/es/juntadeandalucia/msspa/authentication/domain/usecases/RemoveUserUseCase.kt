package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.authentication.data.factory.entities.UserData
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.ParamsSingleUseCase
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.domain.mappers.UserMapper
import es.juntadeandalucia.msspa.authentication.security.CrytographyManager

class RemoveUserUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    ParamsSingleUseCase<Boolean, RemoveUserUseCase.Param>() {

    override fun buildUseCase(params: Param) =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).removeUser(
            params.user, params.cryptoManager
        )

    class Param private constructor(
        val user: UserData,
        val cryptoManager: CrytographyManager?
    ) {

        companion object {
            fun forRemoveUser(msspaAuthenticationUser: MsspaAuthenticationUserEntity, cryptoManager: CrytographyManager?): Param {
                return Param(UserMapper.convert(msspaAuthenticationUser), cryptoManager)
            }
        }
    }
}
