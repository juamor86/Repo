package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import com.google.gson.GsonBuilder
import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import io.reactivex.Completable

class SaveSharedDynamicIconsUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    CompletableUseCase() {

    private lateinit var data: DynamicScreenEntity
    private lateinit var key : String

    fun  params(data: DynamicScreenEntity, key: String) =
        this.apply {
            this.key = key
            this.data = data
        }

    override fun buildUseCase(): Completable =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).saveSharedData(convertDataToJson(data), key)


    private fun convertDataToJson( data: DynamicScreenEntity) : String {
        val gsonBuilder = GsonBuilder().serializeNulls()
        val gson = gsonBuilder.create()

        return gson.toJson(
            data
        )
    }
}