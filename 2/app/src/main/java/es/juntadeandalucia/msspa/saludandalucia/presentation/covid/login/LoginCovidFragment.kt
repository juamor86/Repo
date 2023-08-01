package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.login

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.*
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.KeyValueEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizUserEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.login.adapter.UserAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomBottomSheetDialog
import es.juntadeandalucia.msspa.saludandalucia.security.CrytographyManager
import es.juntadeandalucia.msspa.saludandalucia.utils.BiometricUtils
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import kotlinx.android.synthetic.main.fragment_login_covid.*
import java.util.*
import javax.crypto.Cipher
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class LoginCovidFragment : BaseFragment(), LoginCovidContract.View {

    @Inject
    lateinit var presenter: LoginCovidContract.Presenter

    @set:Inject
    var cryptoManager: CrytographyManager? = null

    private val DEFAULT_DNI_UNDER_AGE = "00000000T"

    private lateinit var usersButton: MenuItem

    private lateinit var usersDialog: CustomBottomSheetDialog<QuizUserEntity>

    private lateinit var scrollListener: ViewTreeObserver.OnScrollChangedListener

    private var user: QuizUserEntity? = null

    private val identifierTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            identifier_type_tv.currentItem?.apply {
                presenter.checkIdentifier(key, identifier_it.editText?.text.toString().trim())
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Does nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Does nothing
        }
    }

    private val nushaIdentifierTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            identifier_type_tv.currentItem?.apply {
                ensureNUHSAPrefix()
                presenter.checkIdentifier(key, identifier_it.editText?.text.toString().trim())
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Does nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Does nothing
        }
    }

    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_login_covid

    override fun injectComponent() {
        DaggerFragmentComponent.builder().applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule()).build().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setupView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.login_covid_menu, menu)
        usersButton = menu.findItem(R.id.users_bt)
        super.onCreateOptionsMenu(menu, inflater)
        presenter.onOptionsMenuCreated()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideLoading()
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    override fun setupView() {
        initRootScroll()
        initIdentifierInput()
        initBirthdayInput()
        initPhonePrefixInput()
        initPhoneInput()
        initQuestions()

        login_covid_bt.setOnClickListener { onSendClicked() }

        overlay.setOnTouchListener { _, _ ->
            presenter.hideAlerts()
            false
        }

        // Uncomment if a fulfilled login form is desired
//        if (BuildConfig.DEBUG) {
//            fillForm(
//                UserEntity(
//                    nuhsa = "AN0211989760",
//                    identification = "92920000T",
//                    idType = KeyValueEntity("1", "DNI/NIF"),
//                    birthDate = "03/03/1970",
//                    phone = "123456789",
//                    prefixPhone = "+34"
//                )
//            )
//        }
    }

    override fun setupIdentifier() {
        identifier_it.apply {
            isHelperTextEnabled = false
            editText?.apply {
                filters = arrayOf(
                    InputFilter.LengthFilter(Consts.MAX_IDENTIFIER_LENGTH),
                    Consts.whiteSpaceFilter
                )
                inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                removeTextChangedListener(nushaIdentifierTextWatcher)
                addTextChangedListener(identifierTextWatcher)
                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        if (user?.identification != DEFAULT_DNI_UNDER_AGE) {
                            identifier_type_tv?.currentItem?.key?.let { type ->
                                presenter.validateIdentifier(
                                    type,
                                    text.toString()
                                )
                            }
                        }
                        hideKeyboard()
                    }
                }
                identifier_type_tv.setOnItemClickListener { _, _, _, _ ->
                    identifier_type_tv?.currentItem?.key?.let { type ->
                        presenter.onIdentifierTypeSelected(
                            type,
                            identifier_it.editText?.text.toString()
                        )
                    }
                }
            }
        }
        identifier_type_tv.setOnClickListener { identifier_type_tv.showDropDown() }
    }

    override fun setNuhsaAsIdentifier() {
        identifier_type_tv.setItem(0)
    }

    override fun setupNushaIdentifier() {
        identifier_it.apply {
            isHelperTextEnabled = true
            helperText = getString(R.string.nuhsa_helper)
            editText?.apply {
                filters = arrayOf(
                    InputFilter.LengthFilter(Consts.MAX_NUHSA_LENGTH),
                    Consts.whiteSpaceFilter
                )
                inputType = InputType.TYPE_CLASS_NUMBER
                removeTextChangedListener(identifierTextWatcher)
                setText(Consts.NUHSA_PREFIX)
                addTextChangedListener(nushaIdentifierTextWatcher)
                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        identifier_type_tv?.currentItem?.key?.let { type ->
                            presenter.validateIdentifier(type, text.toString())
                        }
                        hideKeyboard()
                    }
                }
                setSelection(text?.length ?: 0)
            }
        }
    }

    private fun ensureNUHSAPrefix() {
        identifier_it.editText?.text?.toString()?.apply {
            if (isNotEmpty() && !startsWith(Consts.NUHSA_PREFIX)) {
                val deletedPrefix = Consts.NUHSA_PREFIX.substring(0, Consts.NUHSA_PREFIX.length - 1)
                val cleanString = if (startsWith(deletedPrefix)) {
                    replace(deletedPrefix, "")
                } else {
                    replace(Consts.NUHSA_PREFIX, "")
                }
                identifier_it.editText?.apply {
                    setText("${Consts.NUHSA_PREFIX}$cleanString")
                    setSelection(Consts.NUHSA_PREFIX.length)
                }
            }
        }
    }

    private fun onSendClicked() {
        presenter.onSendClicked(
            idType = identifier_type_tv.currentItem!!,
            identification = identifier_it.editText?.text.toString(),
            birthDate = birthday_it.editText?.text.toString(),
            prefixPhone = phone_prefix_it.editText?.text.toString(),
            phone = phone_it.editText?.text.toString(),
            isHealthProf = q_health_prof_rg_yes.isChecked,
            isSecurityProf = q_security_prof_rg_yes.isChecked,
            isSpecialProf = q_special_prof_rg_yes.isChecked,
            saveUser = login_save_user_cb.isChecked
        )
    }

    private fun initRootScroll() {
        scrollListener = object : ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                if (isSaveUserDataCheckBoxVisible()) {
                    presenter.showSaveUserDataAlertIfNeeded()
                    login_covid_sv.viewTreeObserver.removeOnScrollChangedListener(this)
                }
            }
        }
        login_covid_sv.viewTreeObserver.addOnScrollChangedListener(scrollListener)
    }

    override fun removeScrollListener() {
        login_covid_sv.viewTreeObserver.removeOnScrollChangedListener(scrollListener)
    }

    private fun initBirthdayInput() {
        birthday_it.editText?.apply {
            addTextChangedListener(afterTextChanged = {
                presenter.checkBirthday(text.toString())
            })
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    birthday_dp.visibility = View.GONE
                    presenter.validateBirthday(text.toString())
                } else {
                    hideKeyboard()
                    birthday_dp.visibility = View.VISIBLE
                    login_covid_sv.scrollTo(0, birthday_dp.bottom)
                }
            }
            inputType = InputType.TYPE_NULL
        }
        birthday_dp.apply {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            init(
                calendar.get(Calendar.YEAR) - Consts.BIRTHDAY_MIN_YEARS,
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ) { _, year, month, day ->
                var monthStr = (month + 1).toString()
                if (monthStr.length == 1) {
                    monthStr = "0$monthStr"
                }
                var dayStr = day.toString()
                if (dayStr.length == 1) {
                    dayStr = "0$dayStr"
                }
                birthday_it.editText?.setText("$dayStr/$monthStr/$year")
            }
            maxDate = calendar.timeInMillis
        }
    }

    private fun initIdentifierInput() {
        setupIdentifier()
    }

    override fun setupIdTypesAdapter(idTypes: List<KeyValueEntity>) {
        identifier_type_tv.setAdapter(
            ArrayAdapter(
                requireContext(), android.R.layout.simple_spinner_dropdown_item, idTypes
            )
        )
    }

    private fun initPhonePrefixInput() {
        phone_prefix_it.editText?.apply {
            setText(Consts.SPAIN_PREFIX)
            addTextChangedListener(afterTextChanged = {
                ensurePhonePrefixPlus()
                setMaxLengthPhoneInput()
                presenter.checkPhonePrefix(text.toString())
            })
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    presenter.validatePhonePrefix(text.toString())
                    presenter.validatePhoneNumber(
                        text.toString(),
                        phone_it.editText?.text.toString()
                    )
                    hideKeyboard()
                }
            }
            setSelection(text?.length ?: 0)
        }
    }

    private fun ensurePhonePrefixPlus() {
        phone_prefix_it.editText?.text?.toString()?.apply {
            if (!startsWith(Consts.PLUS)) {
                val cleanString: String
                val deletedPrefix = Consts.PLUS.substring(0, Consts.PLUS.length - 1)
                cleanString = if (startsWith(deletedPrefix)) {
                    replace(deletedPrefix, "")
                } else {
                    replace(Consts.PLUS, "")
                }
                phone_prefix_it.editText?.apply {
                    setText(Consts.PLUS + cleanString)
                    setSelection(Consts.PLUS.length)
                }
            }
        }
    }

    private fun setMaxLengthPhoneInput() {
        phone_et.filters =
            when (phone_prefix_it.editText?.text.toString()) {
                Consts.SPAIN_PREFIX -> arrayOf(InputFilter.LengthFilter(Consts.VALIDATION_MAX_LENGTH_SPANISH_PHONE_NUMBER))
                else -> emptyArray()
            }
    }

    private fun initQuestions() {
        q_health_prof_rg_no.isChecked = true
        q_security_prof_rg_no.isChecked = true
        q_special_prof_rg_no.isChecked = true
    }

    override fun showUsersButton() {
        usersButton.isVisible = true
    }

    override fun hideUsersButton() {
        usersButton.isVisible = false
    }

    override fun isBiometricCompatible(): Boolean {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M).and(
            BiometricUtils.isBiometricOrSecured(
                requireContext()
            )
        )
    }

    override fun showSaveUserCheck() {
        login_save_user_cb.visibility = View.VISIBLE
    }

    override fun hideSaveUserCheck() {
        login_save_user_cb.visibility = View.GONE
    }

    private fun initPhoneInput() {
        phone_it.editText?.apply {
            addTextChangedListener(afterTextChanged = {
                presenter.checkPhoneNumber(
                    phone_prefix_it.editText?.text.toString(),
                    text.toString()
                )
            })
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    presenter.validatePhoneNumber(
                        phone_prefix_it.editText?.text.toString(),
                        text.toString()
                    )
                    hideKeyboard()
                }
            }
        }
    }

    override fun showErrorNuhsa() {
        identifier_it.error = getString(R.string.error_nuhsa)
    }

    override fun showValidBirthday() {
        birthday_it.isErrorEnabled = false
    }

    override fun showErrorBirthday() {
        birthday_it.error = getString(R.string.error_birthday)
    }

    override fun showValidPhonePrefix() {
        phone_prefix_it.isErrorEnabled = false
    }

    override fun showErrorPhonePrefix() {
        phone_prefix_it.error = getString(R.string.error_prefix)
    }

    override fun showInputErrorsDialog() {
        showErrorDialog(R.string.form_input_errors)
    }

    override fun showValidPhoneNumber() {
        phone_it.isErrorEnabled = false
    }

    override fun showErrorPhoneNumber() {
        phone_it.error = getString(R.string.error_phone)
    }

    override fun showValidIdentifier() {
        identifier_it.isErrorEnabled = false
    }

    override fun showErrorIdentifier() {
        identifier_it.error = getString(R.string.identifier_error)
    }

    override fun showErrorDNIIdentifier() {
        identifier_it.error = getString(R.string.dni_error)
    }

    override fun showErrorNIEIdentifier() {
        identifier_it.error = getString(R.string.nie_error)
    }

    override fun showMaxLoginsError() {
        showWarningDialog(title = R.string.error_max_logins)
    }

    override fun showLoginFieldsError() {
        showErrorDialog(R.string.error_login_fields)
    }

    override fun initUsersButton() {
        usersButton.setOnMenuItemClickListener {
            presenter.onUsersButtonClicked()
            true
        }
    }

    override fun authenticateForEncryption(
        onSuccess: (Cipher, Cipher) -> Unit,
        onError: (String) -> Unit
    ) {
        cryptoManager?.promptForEncryption(
            onSuccess,
            onError,
            requireActivity(),
            Consts.KEY_SAVED_USERS
        )
    }

    override fun authenticateForDecryption(
        onSuccess: (Cipher) -> Unit,
        onError: (String) -> Unit
    ) {
        cryptoManager?.promptForDecryption(
            onSuccess,
            onError,
            requireActivity(),
            Consts.KEY_SAVED_USERS
        )
    }

    override fun showSavedUsers(users: List<QuizUserEntity>) {
        val adapter = UserAdapter(
            onClickItemListener = { userEntity, _ -> presenter.onSelectUser(userEntity) },
            onRemoveItemListener = { userEntity ->
                presenter.onRemoveUser(
                    userEntity,
                    cryptoManager
                )
            }
        ).apply {
            setItemsAndNotify(users)
        }
        usersDialog = CustomBottomSheetDialog.newInstance(adapter = adapter)
        usersDialog.show(requireFragmentManager(), "")
    }

    override fun fillForm(user: QuizUserEntity?) {
        user?.let {
            this.user = it

            if (it.prefixPhone.isNullOrEmpty()) {
                it.prefixPhone = Consts.SPAIN_PREFIX
            }
            // Identifier
            if (it.idType.key.isEmpty()) {
                identifier_type_tv.setItem(identifier_type_tv.adapter.getItem(0) as KeyValueEntity)
                identifier_it.editText?.setText(it.nuhsa)
            } else {
                identifier_type_tv.setItem(it.idType)
                identifier_type_tv.setText(it.idType.value, false)
                if (it.identification != DEFAULT_DNI_UNDER_AGE) {
                    identifier_it.editText?.setText(if (it.idType.key == QuizUserEntity.ID_TYPE_NUHSA) it.nuhsa else it.identification)
                } else {
                    identifier_type_tv.setItem(identifier_type_tv.adapter.getItem(0) as KeyValueEntity)
                    identifier_it.editText?.setText(it.nuhsa)
                }
            }
            if (user.identification != DEFAULT_DNI_UNDER_AGE) {
                identifier_type_tv?.currentItem?.key?.let { type ->
                    presenter.validateIdentifier(
                        type,
                        identifier_it.editText?.text.toString()
                    )
                }
            }

            // Birthday
            birthday_it.editText?.setText(it.birthDate)
            presenter.validateBirthday(birthday_it.editText?.text.toString())
            // Phone
            phone_it.editText?.setText(it.phone)
            presenter.validatePhoneNumber(it.prefixPhone!!, phone_it.editText?.text.toString())
            // Prefix
            phone_prefix_it.editText?.setText(it.prefixPhone)
            // Other questions
            if (it.isHealthProf) {
                q_health_prof_rg_yes.isChecked = true
            } else {
                q_health_prof_rg_no.isChecked = true
            }
            if (it.isSecurityProf) {
                q_security_prof_rg_yes.isChecked = true
            } else {
                q_security_prof_rg_no.isChecked = true
            }

            if (it.isSpecialProf) {
                q_special_prof_rg_yes.isChecked = true
            } else {
                q_special_prof_rg_no.isChecked = true
            }
        }
    }

    override fun hideUsersDialog() {
        usersDialog.dismiss()
    }

    override fun showSaveUserDataAlert() {
        with(save_user_advice_tv) {
            requestFocus()
            animateViews(
                this,
                animId = R.anim.grow_in_top_end,
                postAnimation = { visibility = View.VISIBLE }
            )
        }
    }

    override fun showStoredUsersDataAlert() {
        with(load_user_advice_tv) {
            requestFocus()
            animateViews(
                this,
                animId = R.anim.grow_in_bottom_start,
                postAnimation = { visibility = View.VISIBLE }
            )
        }
    }

    override fun hideSaveUserDataAlert() {
        with(save_user_advice_tv) {
            if (isVisible) {
                animateViews(
                    this,
                    animId = R.anim.grow_out_bottom_start,
                    postAnimation = { visibility = View.GONE }
                )
            }
        }
    }

    override fun hideStoredUsersDataAlert() {
        with(load_user_advice_tv) {
            if (isVisible) {
                animateViews(
                    this,
                    animId = R.anim.grow_out_top_end,
                    postAnimation = { visibility = View.GONE }
                )
            }
        }
    }

    private fun isSaveUserDataCheckBoxVisible(): Boolean {
        val scrollView: NestedScrollView? = view?.findViewById(R.id.login_covid_sv)
        val checkBox: CheckBox? = view?.findViewById(R.id.login_save_user_cb)

        return if (scrollView != null && checkBox != null) checkViewVisibleInScroll(
            scrollView,
            checkBox
        ) else false
    }

    override fun showOverlay() {
        overlay.visibility = View.VISIBLE
    }

    override fun hideOverlay() {
        overlay.visibility = View.GONE
    }

    override fun navigateToQuiz(user: QuizUserEntity) {
        val bundle = bundleOf(Consts.ARG_USER to user)
        findNavController().navigate(R.id.action_coronavirus_dest_to_quiz_dest, bundle)
    }

    override fun navigateToSecondFactor(
        authorize: AuthorizeEntity,
        user: QuizUserEntity,
        saveUser: Boolean
    ) {
        val bundle = bundleOf(
            Consts.ARG_USER to user,
            Consts.ARG_SAVE_USER to saveUser,
            Consts.ARG_AUTHORIZE to authorize
        )
        findNavController().navigate(R.id.action_login_to_second_factor, bundle)
    }

    override fun clearIdentifier() {
        identifier_et.setText("")
    }

    override fun showNoBDUDataError() {
        showWarningDialog(title = R.string.no_bdu_data_autocovid )
    }

    override fun showProtectedUserError() {
        showWarningDialog(title = R.string.protected_user_popup_message)
    }
}
