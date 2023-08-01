package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.entities.WalletData
import es.juntadeandalucia.msspa.saludandalucia.data.factory.CovidCertificateRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import io.reactivex.Completable

class SaveCovidCertificateUseCase(private val covidCertificateRepositoryFactory: CovidCertificateRepositoryFactory) :
    CompletableUseCase() {

    private lateinit var covidWallet: WalletData
    private var isCertSaved = false

    override fun buildUseCase(): Completable =
        covidCertificateRepositoryFactory.create(Strategy.DATABASE).run {
            if (isCertSaved)
                updateCertificate(covidWallet)
            else
                saveCovidCertificate(covidWallet)


        }

    fun params(covidWallet: WalletData, isCertSaved: Boolean) = this.apply {
        this.covidWallet = covidWallet
        this.isCertSaved = isCertSaved
    }
}