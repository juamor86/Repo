package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import android.net.Uri
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.Link
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.MonitoringListData
import es.juntadeandalucia.msspa.saludandalucia.data.factory.MonitoringRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.ObservableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.MonitoringListMapper
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import io.reactivex.Observable

class GetMonitoringListUseCase(private val monitoringRepositoryFactory: MonitoringRepositoryFactory) :
    ObservableUseCase<MonitoringListEntity>() {

    private lateinit var id: String

    override fun buildUseCase(): Observable<MonitoringListEntity> =
        getPageAndNext(1).map {
            MonitoringListMapper.convert(it)
        }

    fun setId(id: String): GetMonitoringListUseCase {
        this.id = id
        return this
    }

    private fun getPageAndNext(page: Int): Observable<MonitoringListData> {
        return monitoringRepositoryFactory.create(Strategy.NETWORK).getMonitoringList(id,page)
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
