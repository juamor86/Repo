package es.juntadeandalucia.msspa.saludandalucia.data.repository.persistence

import es.juntadeandalucia.msspa.saludandalucia.data.entities.WalletData
import es.juntadeandalucia.msspa.saludandalucia.data.persistence.CovidCertificateDao
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.CovidCertificateRepository
import io.reactivex.Completable
import io.reactivex.Single

open class CovidCertificateRepositoryDataBaseImpl(private val covidCertificateDao: CovidCertificateDao) :
    CovidCertificateRepository {

    override fun getCertificates(): Single<List<WalletData>> =
      covidCertificateDao.getAllCovidCertificates()


    override fun saveCovidCertificate(wallet: WalletData): Completable =
        covidCertificateDao.insert(wallet)


    override fun findCertificate(id: String): Single<Boolean> =
        covidCertificateDao.findCert(id)


    override fun updateCertificate(wallet: WalletData): Completable =
       covidCertificateDao.update(wallet)

}