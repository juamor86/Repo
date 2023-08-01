package es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice

import es.juntadeandalucia.msspa.saludandalucia.data.factory.AdvicesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceContactEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypeResource
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ValueReferenceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.advice.AdviceRequestMapper
import io.reactivex.Completable

class CreateAdviceUseCase(private val advicesRepositoryFactory: AdvicesRepositoryFactory) :
    CompletableUseCase() {
    private lateinit var phone: String
    private lateinit var nuhsa: String
    private lateinit var typeResource: AdviceTypeResource
    private lateinit var contact: MutableList<AdviceContactEntity>
    private lateinit var sharedContacts: MutableList<ValueReferenceEntity>
    private var contactEmpty: Boolean = false
    private var type: String = ""

    override fun buildUseCase(): Completable =
        advicesRepositoryFactory.create(Strategy.NETWORK).createAdvice(
            AdviceRequestMapper.convertToRequest(
                phone,
                nuhsa,
                contact,
                sharedContacts,
                typeResource,
                type,
                contactEmpty
            )
        )

    fun params(
        phone: String,
        nuhsa: String,
        contact: MutableList<AdviceContactEntity>,
        sharedContacts: MutableList<ValueReferenceEntity>,
        typeResource: AdviceTypeResource,
        type: String,
        contactEmpty: Boolean = false
    ) =
        this.apply {
            this@CreateAdviceUseCase.phone = phone
            this@CreateAdviceUseCase.nuhsa = nuhsa
            this@CreateAdviceUseCase.contact = contact
            this@CreateAdviceUseCase.typeResource = typeResource
            this.type = type
            this.sharedContacts = sharedContacts
            this.contactEmpty = contactEmpty
        }
}