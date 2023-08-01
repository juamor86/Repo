package es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice

import android.content.Context
import es.juntadeandalucia.msspa.saludandalucia.data.factory.AdvicesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceCatalogTypeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.AdviceMapper
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils

class GetAdvicesUseCase(val context: Context, private val advicesRepositoryFactory: AdvicesRepositoryFactory) :
    SingleUseCase<List<AdviceEntity>>() {

    private lateinit var nuhsa: String
    private lateinit var adviceCatalogList: List<AdviceCatalogTypeEntity>

    fun params(nuhsa: String, adviceCatalogList: List<AdviceCatalogTypeEntity>) =
        this.apply {
            this.nuhsa = nuhsa
            this.adviceCatalogList = adviceCatalogList
        }

    override fun buildUseCase() = advicesRepositoryFactory.create(Strategy.NETWORK).run {
        getAdvices(nuhsa).map {
            Utils.sortedByAdviceCatalogList(
                adviceCatalogList, Utils.fillNameContactAdvices(
                    context,
                    AdviceMapper.groupByEntry(AdviceMapper.convert(it))
                ), true
            )
        }
    }
}