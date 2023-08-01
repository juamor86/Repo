package es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice

import es.juntadeandalucia.msspa.saludandalucia.data.factory.AdvicesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import io.reactivex.Completable

class RemoveAdviceUseCase(private val advicesRepositoryFactory: AdvicesRepositoryFactory) : CompletableUseCase() {
    private var id: String = ""

    override fun buildUseCase(): Completable =
            advicesRepositoryFactory.create(Strategy.NETWORK).removeAdvice(id)

    fun params(id: String) =
            this.apply {
                this@RemoveAdviceUseCase.id = id
            }
}