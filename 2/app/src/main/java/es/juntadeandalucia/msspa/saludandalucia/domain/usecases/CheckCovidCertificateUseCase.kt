package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.CovidCertificateRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import io.reactivex.Single

class CheckCovidCertificateUseCase(private val covidCertificateRepositoryFactory: CovidCertificateRepositoryFactory) :
    SingleUseCase<Boolean>() {

    private lateinit var id: String

    fun params(id: String) = this.apply {
        this.id = id
    }


    override fun buildUseCase(): Single<Boolean> = covidCertificateRepositoryFactory.create(Strategy.DATABASE).run {
        findCertificate(id)
    }

}