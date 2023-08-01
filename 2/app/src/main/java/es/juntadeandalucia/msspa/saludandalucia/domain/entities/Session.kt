package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationManager

class Session {
    var msspaAuthenticationEntity: MsspaAuthenticationEntity = MsspaAuthenticationEntity("", "", 0, MsspaAuthenticationManager.Scope.EMPTY, null, null)
    var beneficiaryList = mutableListOf<BeneficiaryEntity>()

    fun createSession(msspaAuthenticationEntity: MsspaAuthenticationEntity) {
        this.msspaAuthenticationEntity = msspaAuthenticationEntity
    }

    fun clearSession() {
        msspaAuthenticationEntity = MsspaAuthenticationEntity("", "", 0, MsspaAuthenticationManager.Scope.EMPTY, null, null)
        beneficiaryList.clear()
    }

    fun isUserAuthenticated() = msspaAuthenticationEntity.scope != MsspaAuthenticationManager.Scope.EMPTY
}
