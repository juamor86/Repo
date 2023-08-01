package es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice

import android.content.Context
import es.juntadeandalucia.msspa.saludandalucia.data.factory.AdvicesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceCatalogTypeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.AdviceMapper
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils

class GetAdvicesReceivedUseCase(val context: Context, private val advicesRepositoryFactory: AdvicesRepositoryFactory) :
    SingleUseCase<List<AdviceEntity>>() {

    private lateinit var phone: String
    private lateinit var adviceCatalogList: List<AdviceCatalogTypeEntity>

    fun params(phone: String, adviceCatalogList: List<AdviceCatalogTypeEntity>) =
        this.apply {
            this.phone = phone
            this.adviceCatalogList = adviceCatalogList
        }

    override fun buildUseCase() = advicesRepositoryFactory.create(Strategy.NETWORK).run {
        getAdvicesReceived(phone).map {
            Utils.sortedByAdviceCatalogList(
                adviceCatalogList, filterAdvicesReceived(
                    Utils.fillNameContactAdvices(
                        context,
                        AdviceMapper.groupByEntry(AdviceMapper.convert(it), true)
                    )
                ), false
            )
        }
    }

    private fun filterAdvicesReceived(advicesReceived: List<AdviceEntity>): List<AdviceEntity> {
        val advices: MutableList<AdviceEntity> = mutableListOf()
        advicesReceived.forEach { adviceReceivedEntity ->
            if (adviceReceivedEntity.entry?.find { it.isOwner } == null) {
                advices.add(adviceReceivedEntity)
            }
        }
        return advices
    }
}