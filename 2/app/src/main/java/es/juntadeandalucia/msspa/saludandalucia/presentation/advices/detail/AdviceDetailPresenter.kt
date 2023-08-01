package es.juntadeandalucia.msspa.saludandalucia.presentation.advices.detail

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.NotExistAdviceException
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.NavBackPressedBus
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.*
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.CreateAdviceChildren
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.GetAdviceCatalogTypePrefUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.RemoveAdviceUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.SaveAdviceUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.advice.BaseAdvicePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.ADVICE_STATUS
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.ADVICE_TYPE_ACTIVE
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.ADVICE_TYPE_OFF
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import retrofit2.HttpException
import timber.log.Timber
import java.net.HttpURLConnection

class AdviceDetailPresenter(
    private val saveAdviceUseCase: SaveAdviceUseCase,
    private val removeAdviceUseCase: RemoveAdviceUseCase,
    private val createAdviceChildrenUseCase: CreateAdviceChildren,
    private val getAdviceCatalogTypePreferencesUseCase: GetAdviceCatalogTypePrefUseCase,
    private val sessionBus: SessionBus,
    private val navBackPressedBus: NavBackPressedBus
) : BaseAdvicePresenter<AdviceDetailContract.View>(),
    AdviceDetailContract.Presenter {

    private var fatherEntry: EntryAdviceEntity? = null
    private var childEntry: MutableList<EntryAdviceEntity> = mutableListOf()
    private var isReceivedRequested = false
    private lateinit var typeId: String


    override fun onCreate(
        nuhsa: String,
        advice: AdviceEntity,
        phoneNumber: String?
    ) {
        view.setupAdapter()
        saveArguments(
            nuhsa = nuhsa,
            advice = advice,
            phoneNumber = phoneNumber
        )
        loadEntry(advice)

        with(navBackPressedBus) {
            execute(onNext = { navBackPressedBus->
                if(navBackPressedBus.isNavBackPressed) {
                    view.onNavBackPressed()
                }
            }, onError = {
                Timber.e(it)
            })
        }
    }

    private fun loadEntry(advice: AdviceEntity) {
        advice.entry?.let { entries ->
            fatherEntry = entries[0]
            if (entries.size > 1) {
                val childEntrySubList = entries.subList(1, entries.size)
                childEntrySubList.map {
                    childEntry.add(it)
                }
            }
        }
    }

    override fun onViewCreated() {
        super<BaseAdvicePresenter>.onViewCreated()
        launchEvent()
        view.configureButtons(fatherEntry?.status ?: "",isOwner(fatherEntry))
        setupFather()
        //TODO reset contacts
        //setupSharedContacts()
        view.showOrHideNewShareContacts(View.GONE)
        displayChannelStatusRequested()
    }

    private fun launchEvent() {
        getAdviceCatalogTypePreferencesUseCase.execute(onSuccess = { typeList ->
            typeId = typeList.find {
                it.text == advice?.entry?.get(0)?.text
            }?.id ?: ""
            sendEvent()
        }, onError = {
            Timber.e(it)
        })
    }

    private fun sendEvent() {
        if (isOwner(fatherEntry)) {
            view.sendEvent(Consts.Analytics.AVISAS_DETAIL_ACCESS_FATHER)
        } else {
            view.sendEvent(Consts.Analytics.AVISAS_DETAIL_ACCESS_CHILDREN)
        }
    }

    private fun displayChannelStatusRequested(){
        fatherEntry?.let {
            if(!it.isOwner && (it.status == AdvicesStatus.REQUESTED.name.lowercase() || it.status == ADVICE_TYPE_OFF )){
                isReceivedRequested = true
            }
        }
        if(isReceivedRequested){
            view.hideChannels()
            return
        }
    }
//TODO reset contacts
   /* private fun setupSharedContacts() {
        with(view) {
            if (isOwner(fatherEntry)) {
                setupContactsRecycler()
                showOrHideNewShareContacts(View.VISIBLE)
                fillSharedContacts()
                if (sharedContacts.isNotEmpty())
                    loadContactsToRecyclerView(sharedContacts)

            } else {
                showOrHideNewShareContacts(View.GONE)
            }
        }
    }

    private fun fillSharedContacts() {
        fatherEntry?.let { entry ->
            sharedWith(entry).let { contacts ->
                if (contacts.isNotEmpty()) {
                    contacts.map { contact ->
                        sharedContacts.add(contact)
                    }
                }
            }
        }
        view.fillSharedContactName(sharedContacts)
    }*/

    override fun loadSharedContactNameUpdated(sharedContacts: MutableList<ValueReferenceEntity>) {
        this.sharedContacts = sharedContacts
    }

    override fun checkChanges(
        appSwitch: Boolean,
        emailSwitch: Boolean,
        smsSwitch: Boolean,
        email: String
    ) {
        byApp = appSwitch
        byEmail = emailSwitch
        bySMS = smsSwitch
        this.email = email
        if (isExistChanges()) {
            view.showDialogAdviceNotSaved()
        } else {
            view.navigateToAdviceList()
        }
    }

    override fun onDeleteClicked(
        appSwitch: Boolean,
        emailSwitch: Boolean,
        smsSwitch: Boolean,
        email: String
    ) {
        byApp = appSwitch
        byEmail = emailSwitch
        bySMS = smsSwitch
        this.email = email
        if (isOwner(fatherEntry)) {
            deleteAdvice()
        } else {
            deleteChildren()
        }
    }

    override fun acceptedDelegatedClick() {
        confirmDelegated()
    }

    private fun deleteChildren() {
        view.showLoadingBlocking()
        fatherEntry?.let { adv ->
            saveAdviceUseCase
                .params(
                    nuhsa,
                    adv,
                    getActualChannelState(),
                    sharedContacts,
                    advice!!,
                    ADVICE_TYPE_OFF,
                    isOwner(fatherEntry)
                ).execute(onComplete = {
                    view.apply {
                        sendEvent(Consts.Analytics.AVISAS_REJECT_SUCESS)
                        hideLoading()
                        showDialogAdviceSuccessSaved()
                    }
                }, onError = {
                    view.apply {
                        sendEvent(Consts.Analytics.AVISAS_REJECT_FAILURE)
                        hideLoading()
                    }
                    Timber.e(it)
                    showOnError(it)
                })
        }
    }

    private fun setupFather() {
        with(view) {
            fatherEntry?.let { entry ->
                val title = entry.extension.find { it.url == "evento" }?.valueReference?.display ?: ""
                setupAdviceTitle(title, entry.text)
                setupAdvicesSwitches(entry)

                sharedBy(entry).let { sharedContact ->
                    if (sharedContact.isNotEmpty() && !isOwner(entry)) {
                        setupRequestBy(sharedContact)
                    } else {
                        setupRequestBy(null)
                    }
                }
            }
        }
    }

    override fun onSaveButtonPressed(
        appSwitch: Boolean,
        emailSwitch: Boolean,
        smsSwitch: Boolean,
        email: String
    ) {

        byApp = appSwitch
        byEmail = emailSwitch
        bySMS = smsSwitch
        this.email = email

        view.disableSaveButton()

        if(isAnySwitchChanged() || !isOwner(fatherEntry)){
            if(byEmail) {
                if(Utils.isEmailValid(email) && email.isNotEmpty()){
                    modifyChannel()
                }else{
                    view.apply {
                        enableSaveButton()
                        showEmailError()
                    }
                }
            }else{
                if(isReceivedRequested){
                    view.apply {
                        enableSaveButton()
                        showConfirmDelegated()
                    }
                }else{
                    modifyChannel()
                }
            }
        }else{
            doGroupByChildren(getGroupByChildren())
        }

    }

    private fun modifyChannel() {
        view.showLoadingBlocking()
        fatherEntry?.let { adv ->
            saveAdviceUseCase
                .params(
                    nuhsa,
                    adv,
                    getActualChannelState(),
                    sharedContacts,
                    advice!!,
                    ADVICE_TYPE_ACTIVE,
                    isOwner(fatherEntry)
                )
                .execute(
                    onComplete = {
                        val contacts = getGroupByChildren()
                        if (contacts.isEmpty()) {
                            if (isOwner(fatherEntry)) {
                                view.sendEvent(Consts.Analytics.AVISAS_MODIFIED_FATHER_SUCCESS)
                            } else {
                                view.sendEvent(Consts.Analytics.AVISAS_MODIFIED_CHILDREN_SUCCESS)
                            }
                        }
                        isSaving = false
                        adv.contact = getContactData()
                        doGroupByChildren(contacts)
                    },
                    onError = {
                        view.apply {
                            if (isOwner(fatherEntry)) {
                                sendEvent(Consts.Analytics.AVISAS_MODIFIED_FATHER_FAILURE)
                            } else {
                                sendEvent(Consts.Analytics.AVISAS_MODIFIED_CHILDREN_FAILURE)
                            }
                            enableSaveButton()
                            hideLoading()
                        }
                        Timber.e(it)
                        showOnError(it)
                    }
                )
        }
    }

    private fun confirmDelegated() {
        view.showLoadingBlocking()
        fatherEntry?.let { adv ->
            saveAdviceUseCase
                .params(
                    nuhsa,
                    adv,
                    getActualChannelState(),
                    sharedContacts,
                    advice!!,
                    ADVICE_TYPE_ACTIVE,
                    false
                )
                .execute(
                    onComplete = {
                        adv.contact = getContactData()
                        view.apply {
                            sendEvent(Consts.Analytics.AVISAS_ADVICE_ACCEPTED_SUCCESS)
                            hideLoading()
                            isReceivedRequested = false
                            showConfimationDelegatedSuccess()
                        }
                    },
                    onError = {
                        view.apply {
                            sendEvent(Consts.Analytics.AVISAS_ADVICE_ACCEPTED_FAILURE)
                            hideLoading()
                        }
                        Timber.e(it)
                        showOnError(it)
                    }
                )
        }
    }

    private fun getGroupByChildren(): MutableList<AdviceChildrenEntity> {
        val sharedEntryList = fatherEntry?.extension?.filter {
            it.url == Consts.SHARED_SUBSCRIPTION
        }
        val contactList = mutableListOf<AdviceChildrenEntity>()

        sharedEntryList?.forEach { fatherShared ->
            if (sharedContacts.find { it.display == fatherShared.valueReference.display } == null) {
                contactList.add(
                    AdviceChildrenEntity(
                        AdviceChildrenType.DELETE,
                        fatherShared.valueReference
                    )
                )
            }
        }

        sharedContacts.forEach { shared ->
            if (sharedEntryList?.find { it.valueReference.display == shared.display } == null) {
                contactList.add(AdviceChildrenEntity(AdviceChildrenType.ADD, shared))
            }
        }
        return contactList
    }

    private fun makeChildrenOperations(index: Int = 0, contactList: List<AdviceChildrenEntity>) {
        if (index < contactList.size) {
            view.showLoadingBlocking()
            if (contactList[index].type == AdviceChildrenType.DELETE) {
                removeAdviceUseCase
                    .params(contactList[index].valueReference.id)
                    .execute(
                        onComplete = {
                            makeChildrenOperations(index + 1, contactList)
                        },
                        onError = {
                            Timber.e(it)
                            showOnError(it)
                        }
                    )
            } else {
                createAdviceChildrenUseCase.params(
                    father = fatherEntry!!,
                    nuhsa = nuhsa,
                    contact = contactList[index].valueReference,
                    phoneNumber = phoneNumber
                ).execute(onComplete = {
                    makeChildrenOperations(index + 1, contactList)
                }, onError = {
                    view.apply {
                        if (isOwner(fatherEntry)) {
                            sendEvent(Consts.Analytics.AVISAS_MODIFIED_FATHER_FAILURE)
                        } else {
                            sendEvent(Consts.Analytics.AVISAS_MODIFIED_CHILDREN_FAILURE)
                        }
                    }
                    Timber.e(it)
                    showOnError(it)
                })
            }

        } else {
            view.apply {
                enableSaveButton()
                hideLoading()
                if (isOwner(fatherEntry)) {
                    sendEvent(Consts.Analytics.AVISAS_MODIFIED_FATHER_SUCCESS)
                } else {
                    sendEvent(Consts.Analytics.AVISAS_MODIFIED_CHILDREN_SUCCESS)
                }
                showDialogAdviceSuccessSaved()
            }
        }
    }

    private fun getActualChannelState(): List<AdviceContactEntity> {
        val contacts: MutableList<AdviceContactEntity> = mutableListOf()
        contacts.add(getPhoneContact())
        getSmsContact()?.let { contacts.add(it) }
        getEmailContact()?.let {
            contacts.add(it)
        }
        return contacts
    }

    private fun getPhoneContact(): AdviceContactEntity {
        val valueCode = if (byApp) {
            ADVICE_TYPE_ACTIVE
        } else {
            ADVICE_TYPE_OFF
        }
        return AdviceContactEntity(
            system = ContactTypesEntity.PHONE.name.lowercase(),
            value = phoneNumber,
            use = Consts.MOBILE,
            extension = listOf(
                ContactExtensionEntity(url = ADVICE_STATUS, valueCode = valueCode)
            )
        )
    }

    private fun getSmsContact(): AdviceContactEntity? {
        var contact: AdviceContactEntity? = null
        var valueCode: String

        fatherEntry?.let{
            if (!bySMS && !isContactUsed(it, ContactTypesEntity.SMS)) {
                return contact
            }
            valueCode = if (bySMS) {
                ADVICE_TYPE_ACTIVE
            } else {
                ADVICE_TYPE_OFF
            }
            return AdviceContactEntity(
                system = ContactTypesEntity.SMS.name.lowercase(),
                value = phoneNumber,
                use = Consts.MOBILE,
                extension = listOf(
                    ContactExtensionEntity(url = ADVICE_STATUS, valueCode = valueCode)
                )
            )
        }
        return contact
    }

    private fun getEmailContact(): AdviceContactEntity? {
        var contact: AdviceContactEntity? = null
        var valueCode: String

        fatherEntry?.let { entry ->
                valueCode = if (isContactUsed(entry, ContactTypesEntity.EMAIL) && !byEmail) {
                    ADVICE_TYPE_OFF
                } else {
                    ADVICE_TYPE_ACTIVE
                }
                if (email.isNotEmpty()) {
                    contact = AdviceContactEntity(
                        system = ContactTypesEntity.EMAIL.name.lowercase(),
                        value = email,
                        use = Consts.MOBILE,
                        extension = listOf(
                            ContactExtensionEntity(url = ADVICE_STATUS, valueCode = valueCode)
                        )
                    )
                }
        }

        return contact
    }

    private fun isAnySwitchChanged(): Boolean {
        fatherEntry?.let { entry ->
            val appChanged = byApp != isContactUsed(entry, ContactTypesEntity.PHONE)
            val emailChanged = isEmailChanged()
            val smsChanged = bySMS != isContactUsed(entry, ContactTypesEntity.SMS)
            return appChanged || emailChanged || smsChanged
        }
        return false
    }

    private fun isEmailChanged(): Boolean {
        return if(byEmail && fatherEntry?.let { isContactUsed(it, ContactTypesEntity.EMAIL) } == true){
            email != getEmailContact(fatherEntry?.contact)
        }else{
            byEmail != fatherEntry?.let { isContactUsed(it, ContactTypesEntity.EMAIL) }
        }
    }

    override fun onRemoveButtonPressed() {
        view.showDeleteConfirmation()
    }

    private fun deleteAdvice() {
        view.showLoadingBlocking()
        fatherEntry?.let { it ->
            removeAdviceUseCase
                .params(it.id)
                .execute(
                    onComplete = {
                        view.apply {
                            sendEvent(Consts.Analytics.AVISAS_DELETED_SUCESS)
                            hideLoading()
                            navigateToAdviceList()
                        }
                    },
                    onError = {
                        view.apply {
                            sendEvent(Consts.Analytics.AVISAS_DELETED_FAILURE)
                            hideLoading()
                        }
                        Timber.e(it)
                        showOnError(it)
                    }
                )
        }
    }

    override fun isContactUsed(entry: EntryAdviceEntity, type: ContactTypesEntity): Boolean {
        var used = false

        entry.contact?.let { contacts ->
            for (contact in contacts) {
                if (contact.system == type.name.lowercase()) {
                    if (contact.extension[0].valueCode == ADVICE_TYPE_OFF) {
                        return false
                    }
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

    override fun unsubscribe() {
        saveAdviceUseCase.clear()
        removeAdviceUseCase.clear()
        createAdviceChildrenUseCase.clear()
        getAdviceCatalogTypePreferencesUseCase.clear()
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
            is NotExistAdviceException -> {
                view.showErrorNotExistAdvice()
            }
            else -> sessionBus.showErrorAndHome()

        }
    }

    private fun doGroupByChildren(contactList: List<AdviceChildrenEntity>){
        if (contactList.isNotEmpty()) {
            makeChildrenOperations(contactList = contactList)
        }else{
            view.apply {
                hideLoading()
                showDialogAdviceSuccessSaved()
                enableSaveButton()
            }
        }
    }

    private fun isExistChanges(): Boolean{
        return isAnySwitchChanged() || getGroupByChildren().isNotEmpty()
    }
}