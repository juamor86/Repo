package es.juntadeandalucia.msspa.saludandalucia.presentation.advices.create

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceContactEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypeResource
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ContactTypesEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EntryAdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.CreateAdviceUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.GetAdviceCatalogTypePrefUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.advice.BaseAdvicePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import retrofit2.HttpException
import timber.log.Timber
import java.net.HttpURLConnection

class AdviceCreatePresenter(
    private val createAdviceUseCase: CreateAdviceUseCase,
    private val sessionBus: SessionBus,
    private val getAdviceCatalogTypePreferencesUseCase: GetAdviceCatalogTypePrefUseCase
) : BaseAdvicePresenter<AdviceCreateContract.View>(), AdviceCreateContract.Presenter {

    private lateinit var typeId: String

    override fun onCreate(
        nuhsa: String,
        adviceTypeResource: AdviceTypeResource,
        phoneNumber: String?
    ) {
        view.setupAdapter()
        saveArguments(
            nuhsa = nuhsa,
            adviceType = adviceTypeResource,
            phoneNumber = phoneNumber
        )
        getAdviceCatalogTypePreferencesUseCase.execute(onSuccess = { typeList->
            typeId = typeList.find { it.text == adviceTypeResource.text }?.id.toString().trim()
        }, onError = {})
    }

    override fun onViewCreated() {
        super<BaseAdvicePresenter>.onViewCreated()
        view.apply {
            configureButtons()
            setupButtonsListeners()

            //New Advice
            adviceType?.let {
                setupAdviceTitle(it.text, it.reason)
                showOrHideNewShareContacts(View.GONE)
                setupRequestBy(null)
                setupContactsRecycler()
               // showOrHideNewShareContacts(View.VISIBLE) TODO this line is duplicated see line 51
                isNewAdvice = true
            }
        }
    }

    override fun onCreateButtonPressed(
        appSwitch: Boolean,
        emailSwitch: Boolean,
        smsSwitch: Boolean,
        email: String
    ) {
        this.byApp = appSwitch
        this.byEmail = emailSwitch
        this.bySMS = smsSwitch
        this.email = email
        if (byEmail) {
            if (Utils.isEmailValid(email) && email.isNotEmpty()) {
                createAdvice()
            } else {
                view.showEmailError()
            }
        } else {
            createAdvice()
        }
    }

    override fun isContactUsed(
        entry: EntryAdviceEntity,
        type: ContactTypesEntity
    ): Boolean {
        var used = false

        entry.contact?.let { contacts ->
            for (contact in contacts) {
                if (contact.system == type.toString()) {
                    used = true
                    break
                }
            }
        }

        return used
    }

    override fun getEmailContact(contacts: List<AdviceContactEntity>?): String {
        return contacts?.first { it.system == ContactTypesEntity.EMAIL.name.lowercase() }?.value
            ?: ""
    }

    private fun createAdvice() {
        view.apply {
            disableSaveButton()
            showLoadingBlocking()
        }
        adviceType?.let {

            val contactEmpty = !byApp && !bySMS && !byEmail

            createAdviceUseCase
                .params(phoneNumber, nuhsa, getContactData(), sharedContacts, it, getTypeAdvice(), contactEmpty)
                .execute(
                    onComplete = {
                        view.apply {
                            hideLoading()
                            sendEvent(Consts.Analytics.AVISAS_CREATE_ADVICE_SUCCESS)
                            showDialogAdviceSuccessSaved()
                        }
                    },
                    onError = { error ->
                        view.apply {
                            sendEvent(Consts.Analytics.AVISAS_CREATE_ADVICE_FAILURE)
                            enableSaveButton()
                            hideLoading()
                        }
                        Timber.e(error)
                        showOnError(error)
                    }
                )
        }
    }

    override fun unsubscribe() {
        createAdviceUseCase.clear()
    }

    private fun getTypeAdvice(): String {
        return if (sharedContacts.isEmpty()) {
            "message"
        } else {
            "transaction"
        }
    }

    private fun showOnError(error: Throwable) {
        when (error) {
            is HttpException -> {
                when (error.code()) {
                    HttpURLConnection.HTTP_FORBIDDEN, HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        sessionBus.onUnauthorizedEvent()
                    }
                    else -> sessionBus.showErrorAndHome()
                }
            }
            else -> sessionBus.showErrorAndHome()
        }
    }
}