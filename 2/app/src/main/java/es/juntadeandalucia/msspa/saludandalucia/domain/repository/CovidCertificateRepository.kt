package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.WalletData
import io.reactivex.Completable
import io.reactivex.Single

interface CovidCertificateRepository {

    fun findCertificate(id: String) : Single<Boolean>

    fun getCertificates(): Single<List<WalletData>>

    fun saveCovidCertificate(wallet: WalletData): Completable

    fun updateCertificate( wallet: WalletData) : Completable
}