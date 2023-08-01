package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.CovidCertificateRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.WalletEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.CertificateMapper
import javax.crypto.Cipher

class GetCovidCertificatesUseCase(private val covidCertificateRepositoryFactory: CovidCertificateRepositoryFactory) :
    SingleUseCase<List<WalletEntity>>() {

    override fun buildUseCase() =
        covidCertificateRepositoryFactory.create(Strategy.DATABASE)
            .getCertificates().map {
                CertificateMapper.convert(it)
            }
}