package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import android.content.Context
import es.juntadeandalucia.msspa.saludandalucia.data.persistence.CovidCertificateDao
import es.juntadeandalucia.msspa.saludandalucia.data.repository.persistence.CovidCertificateRepositoryDataBaseImpl

class CovidCertificateRepositoryMockImpl(
    val context: Context,
    covidCertificateDao: CovidCertificateDao
) :
    CovidCertificateRepositoryDataBaseImpl(covidCertificateDao) {

}