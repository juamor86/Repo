package es.juntadeandalucia.msspa.saludandalucia.presentation.wallet.detail

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.WalletEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class WalletDetailContract {

    interface View: BaseContract.View {
        fun showCert(cert: WalletEntity, title: String, idValue: String?, idType: String?)
    }

    interface Presenter: BaseContract.Presenter {
        fun setupView(cert: WalletEntity)
    }
}