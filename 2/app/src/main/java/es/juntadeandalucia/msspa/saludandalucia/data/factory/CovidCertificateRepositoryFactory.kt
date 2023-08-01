package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.CovidCertificateRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.persistence.CovidCertificateRepositoryDataBaseImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.CovidCertificateRepository

class CovidCertificateRepositoryFactory(
    private val covidCertificateRepositoryDataBaseImpl: CovidCertificateRepositoryDataBaseImpl,
    private val covidCertificateRepositoryMockImpl: CovidCertificateRepositoryMockImpl
):BaseRepositoryFactory<CovidCertificateRepository>() {

    override fun create(strategy: Strategy): CovidCertificateRepository =
        when(strategy){
            Strategy.DATABASE->covidCertificateRepositoryDataBaseImpl
            else-> covidCertificateRepositoryMockImpl
        }


}