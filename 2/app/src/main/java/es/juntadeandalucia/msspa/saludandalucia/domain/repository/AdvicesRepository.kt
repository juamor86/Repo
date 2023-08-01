package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceRequestData
import io.reactivex.Completable
import io.reactivex.Single

interface AdvicesRepository {
    fun getAdvices(nuhsa: String): Single<AdviceData>
    fun getAdvicesReceived(phone: String): Single<AdviceData>
    fun updateAdvice(id: String, advice: AdviceRequestData.Entry.Resource): Completable
    fun removeAdvice(id: String): Completable
    fun createAdvice(advice: AdviceRequestData): Completable
}