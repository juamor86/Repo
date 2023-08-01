package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.LikeItResponseData
import io.reactivex.Single

interface FeedbackRepository {
    fun getEvents(): Single<LikeItResponseData>
}