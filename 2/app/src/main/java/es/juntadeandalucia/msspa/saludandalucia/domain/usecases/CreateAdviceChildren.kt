package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.AdvicesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceContactEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypeResource
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EntryAdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ValueReferenceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.advice.AdviceRequestMapper
import io.reactivex.Completable

class CreateAdviceChildren(private val advicesRepositoryFactory: AdvicesRepositoryFactory) :
    CompletableUseCase() {

    private lateinit var contact: ValueReferenceEntity
    private lateinit var father: EntryAdviceEntity
    private lateinit var nuhsa: String
    private lateinit var phoneNumber: String


    override fun buildUseCase(): Completable =
        advicesRepositoryFactory.create(Strategy.NETWORK).createAdvice(
            AdviceRequestMapper.createChildrenRequestObject(
                father, contact, nuhsa,phoneNumber
            )
        )

    fun params(
        father: EntryAdviceEntity,
        contact: ValueReferenceEntity,
        nuhsa: String,
        phoneNumber: String
    ) =
        this.apply {
            this@CreateAdviceChildren.contact = contact
            this@CreateAdviceChildren.father = father
            this@CreateAdviceChildren.nuhsa = nuhsa
            this@CreateAdviceChildren.phoneNumber = phoneNumber
        }
}