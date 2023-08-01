package es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice


import es.juntadeandalucia.msspa.saludandalucia.data.factory.AdvicesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceContactEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EntryAdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ValueReferenceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.advice.AdviceRequestMapper
import io.reactivex.Completable

class SaveAdviceUseCase(private val advicesRepositoryFactory: AdvicesRepositoryFactory) :
    CompletableUseCase() {
    private lateinit var nuhsa: String
    private lateinit var status: String
    private lateinit var entryAdvice: EntryAdviceEntity
    private lateinit var advice: AdviceEntity
    private lateinit var contact: List<AdviceContactEntity>
    private lateinit var sharedContacts: MutableList<ValueReferenceEntity>
    private var isDelegated: Boolean = true

    override fun buildUseCase(): Completable =
        advicesRepositoryFactory.create(Strategy.NETWORK).updateAdvice(
            entryAdvice.id,
            AdviceRequestMapper.convertResource(
                id = entryAdvice.id,
                nuhsa = nuhsa,
                contact = contact,
                model = entryAdvice,
                sharedContacts = sharedContacts,
                status = status,
                isDelegate = !isDelegated
            )
        )

    fun params(
        nuhsa: String,
        entryAdvice: EntryAdviceEntity,
        contact: List<AdviceContactEntity>,
        sharedContacts: MutableList<ValueReferenceEntity>,
        advice: AdviceEntity,
        status: String,
        isDelegated: Boolean = true
    ) =
        this.apply {
            this@SaveAdviceUseCase.nuhsa = nuhsa
            this@SaveAdviceUseCase.entryAdvice = entryAdvice
            this.contact = contact
            this.advice = advice
            this.sharedContacts = sharedContacts
            this.status = status
            this.isDelegated = isDelegated
        }
}