package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import android.net.Uri
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.DynQuestListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.Link
import es.juntadeandalucia.msspa.saludandalucia.data.factory.DynamicQuestionnairesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.ObservableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestListEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.dynamic_questionnaires.DynQuestListMapper
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import io.reactivex.Observable

class GetDynQuestListUseCase(private val dynQuestRepositoryFactory: DynamicQuestionnairesRepositoryFactory) :
    ObservableUseCase<DynQuestListEntity>() {

    private lateinit var id: String

    override fun buildUseCase(): Observable<DynQuestListEntity> =
        getPageAndNext(1).map {
            DynQuestListMapper.convert(it)
        }

    fun setId(id: String): GetDynQuestListUseCase {
        this.id = id
        return this
    }

    private fun getPageAndNext(page: Int): Observable<DynQuestListData> {
        return dynQuestRepositoryFactory.create(Strategy.NETWORK).getDynamicQuestList(id,page)
            .toObservable()
            .flatMap {
                if (thereIsNextPage(page, it.link)) {
                    Observable.just(it).concatWith(getPageAndNext(page + 1))
                } else {
                    Observable.just(it)
                }
            }
    }

    private fun thereIsNextPage(page: Int, link: List<Link>): Boolean {
        for (aux in link) {
            if (aux.relation == Consts.LAST) {
                Uri.parse(aux.url).getQueryParameter(Consts.PAGE)?.let {
                    return page < Integer.parseInt(it)
                }
            }
        }
        return false
    }
}