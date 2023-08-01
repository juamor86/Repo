package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.authentication.utils.Utils.Companion.deleteAliasKeyStore
import javax.crypto.Cipher

class RemoveAllUsersUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    CompletableUseCase() {

    private lateinit var cipherEncrypt: Cipher
    private lateinit var cipherDecrypt: Cipher
    private var isCipherInvalid: Boolean = false
    private var aliasKeyStore: String = ""

    fun params(
        cEncrypt: Cipher? = null,
        cipherDecrypt: Cipher? = null,
        isCipherInvalid: Boolean? = null,
        aliasKeyStore: String? = null
    ) {
        this.apply {
            cEncrypt?.let {
                this.cipherEncrypt = it
            }
            cipherDecrypt?.let {
                this.cipherDecrypt = it
            }
            isCipherInvalid?.let {
                this.isCipherInvalid = it
            }
            aliasKeyStore?.let {
                this.aliasKeyStore = it
            }
        }
    }

    override fun buildUseCase() = preferencesRepositoryFactory.create(Strategy.PREFERENCES).run {
       if(isCipherInvalid){
           deleteAliasKeyStore(aliasKeyStore)
           removeAllUser()
        }else{
            removeAllUser(cipherEncrypt, cipherDecrypt)
        }
    }
}
