package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceCatalogTypeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.AdviceCatalogTypeMapper
import io.reactivex.Completable

class SaveAdviceTypeCatalogUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) : CompletableUseCase() {

    private lateinit var adviceTypeCatalogList: List<AdviceCatalogTypeEntity>

    override fun buildUseCase(): Completable =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).saveAdviceCatalogType(
            AdviceCatalogTypeMapper.convertListData(adviceTypeCatalogList))

    fun params(adviceTypeCatalogList: List<AdviceCatalogTypeEntity>) =
        this.apply {
            this@SaveAdviceTypeCatalogUseCase.adviceTypeCatalogList = adviceTypeCatalogList
        }
}
