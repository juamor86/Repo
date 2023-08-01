package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.FeaturedData
import io.reactivex.Single

interface FeaturedRepository {

    fun getFeatured(): Single<List<FeaturedData>>
}
