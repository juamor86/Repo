package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata

import android.os.Build
import android.os.Bundle
import android.text.*
import android.view.*
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.HealthCardEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.KeyValueEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.authentication.presentation.dialog.MsspaAuthCustomBottomSheetDialog
import es.juntadeandalucia.msspa.authentication.presentation.login.adapter.UserAdapter
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import es.juntadeandalucia.msspa.authentication.utils.BiometricUtils
import es.juntadeandalucia.msspa.authentication.utils.Utils
import es.juntadeandalucia.msspa.authentication.utils.isViewVisible
import kotlinx.android.synthetic.main.fragment_login_base.birthday_dp
import kotlinx.android.synthetic.main.fragment_login_base.birthday_et
import kotlinx.android.synthetic.main.fragment_login_base.birthday_it
import kotlinx.android.synthetic.main.fragment_login_base.health_card_it
import kotlinx.android.synthetic.main.fragment_login_base.identifier_et
import kotlinx.android.synthetic.main.fragment_login_base.identifier_it
import kotlinx.android.synthetic.main.fragment_login_base.identifier_type_tv
import kotlinx.android.synthetic.main.fragment_login_base.load_user_advice_tv
import kotlinx.android.synthetic.main.fragment_login_base.login_bt
import kotlinx.android.synthetic.main.fragment_login_base.overlay
import kotlinx.android.synthetic.main.fragment_login_base.root_sv
import kotlinx.android.synthetic.main.fragment_login_base.save_user_advice_tv
import kotlinx.android.synthetic.main.fragment_login_base.save_user_ch
import java.util.*

//TODO: Create two abstract clase one could be this with nuhsa and remember check and other class without this
abstract class LoginBaseFragment : BaseFragment(), LoginBaseContract.View {

    private val presenter by lazy { bindPresenter() }

    abstract override fun bindPresenter(): LoginBaseContract.Presenter

    private lateinit var usersButton: MenuItem

    private val identifierTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(currentText: Editable?) {
            identifier_type_tv.currentItem?.apply {
                presenter.checkIdentifier(key, identifier_it.editText?.text.toString(), birthday_it.editText?.text.toString())
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
        override fun afterTextChanged(currentText: Editable?) {
            presenter.checkIdentifier(MsspaAuthenticationUserEntity.ID_TYPE_NUSS_NUHSA, health_card_it.editText?.text.toString(), "")
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Does nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Does nothing
        }
    }

    private lateinit var usersDialogMsspaAuth: MsspaAuthCustomBottomSheetDialog<MsspaAuthenticationUserEntity>
    //endregion

    //region INITIALIZATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(requireArguments()) {
            getParcelable<AuthorizeEntity>(ApiConstants.Arguments.ARG_AUTHORIZE)?.let { authorizeEntity ->
                presenter.onViewCreated(authorizeEntity)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.login_personal_information, menu)
        usersButton = menu.findItem(R.id.users_bt)
        super.onCreateOptionsMenu(menu, inflater)
        presenter.onOptionsMenuCreated()
    }

    override fun setupView() {
        initCheckSaveUsers()
        checkSaveUsersCheckIsVisible()
        initIdentifierInput()
        initBirthdayInput()
        login_bt.setOnClickListener { onSendClicked() }
        setupIdentifier()
        setupNuhsaIdentifier()
    }
    
    private fun initCheckSaveUsers(){
        save_user_ch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                presenter.requestBiometrics()
            }
        }
    }

    private fun checkSaveUsersCheckIsVisible() {
        save_user_ch?.let {
            if (it.isShown) {
                presenter.showSaveUserDataAlertIfNeeded()
            }
        }

        val scrollListener = object : ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                if(save_user_ch == null) return
                if (root_sv?.isViewVisible(save_user_ch) == true) {
                    presenter.showSaveUserDataAlertIfNeeded()
                    root_sv.viewTreeObserver.removeOnScrollChangedListener(this)
                }
            }
        }
        root_sv.viewTreeObserver.addOnScrollChangedListener(scrollListener)
    }
    //endregion

    //region METHODS AND EVENTS
    override fun showErrorDNIIdentifier() {
        identifier_it.error = resources.getString(R.string.msspa_auth_dni_error)
    }

    override fun showErrorIdentifier() {
        identifier_it.error = resources.getString(R.string.msspa_auth_identifier_error)
    }

    override fun showErrorNIEIdentifier() {
        identifier_it.error = resources.getString(R.string.msspa_auth_nie_error)
    }

    override fun showErrorNuhsa() {
        health_card_it.error = resources.getString(R.string.msspa_auth_error_nuhsa)
    }

    override fun showValidIdentifier() {
        identifier_it.error = null
    }

    override fun showValidNuhsa() {
        health_card_it.error = null
    }

    override fun setupIdTypesAdapter(idTypes: List<KeyValueEntity>) {
        activity?.let { act ->
            identifier_type_tv.setAdapter(
                    ArrayAdapter(act, android.R.layout.simple_spinner_dropdown_item, idTypes)
            )
        }

        identifier_type_tv.setItem(0)
    }

    override fun clearIdentifier() {
        identifier_et.setText("")
    }

    private fun setupIdentifier() {
        identifier_it.apply {
            isHelperTextEnabled = false
            editText?.apply {
                filters = arrayOf(InputFilter.LengthFilter(MsspaAuthConsts.MAX_IDENTIFIER_LENGTH), Utils.whiteSpaceFilter)
                inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                addTextChangedListener(identifierTextWatcher)
                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        presenter.validateIdentifier(
                                identifier_type_tv.currentItem!!.key,
                                text.toString(), birthday_et.text.toString()
                        )
                    }
                }
                identifier_type_tv.setOnItemClickListener { _, _, _, _ ->
                    presenter.onIdentifierTypeSelected(
                            identifier_type_tv.currentItem!!.key,
                            identifier_it.editText?.text.toString()
                    )
                }
            }
        }
        identifier_type_tv.setOnClickListener { identifier_type_tv.showDropDown() }
    }

    private fun setupNuhsaIdentifier() {
        health_card_it?.apply {
            isHelperTextEnabled = true
            helperText = getString(R.string.msspa_auth_nuhsa_helper)
            editText?.apply {
                filters = arrayOf(InputFilter.LengthFilter(MsspaAuthConsts.MAX_NUSS_LENGHT), InputFilter.AllCaps(), Utils.whiteSpaceFilter)
                inputType = InputType.TYPE_CLASS_TEXT
                addTextChangedListener(nushaIdentifierTextWatcher)
                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        presenter.validateIdentifier(
                                "0",
                                text.toString(), ""
                        )
                    }
                }
                setSelection(text?.length ?: 0)
            }
        }
    }

    override fun showErrorBirthday() {
        birthday_it?.error = getString(R.string.msspa_auth_error_birthday)
    }

    override fun showValidBirthday() {
        birthday_it?.isErrorEnabled = false
    }

    private fun initIdentifierInput() {
        setupIdentifier()
    }

    private fun initBirthdayInput() {
        birthday_it?.editText?.apply {
            addTextChangedListener(afterTextChanged = {
                presenter.checkBirthday(text.toString(), identifier_et.text.toString())
            })
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    birthday_dp.visibility = View.GONE
                    presenter.validateBirthday(text.toString())
                } else {
                    hideKeyboard()
                    birthday_dp.visibility = View.VISIBLE
                }
            }
            inputType = InputType.TYPE_NULL
        }
        birthday_dp?.apply {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            init(
                    calendar.get(Calendar.YEAR) - MsspaAuthConsts.BIRTHDAY_MIN_YEARS,
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

    override fun fillForm(msspaAuthenticationUser: MsspaAuthenticationUserEntity?) {
        msspaAuthenticationUser?.let { it ->
            health_card_it?.apply {
                editText?.setText(if (it.nuss.isNotEmpty()) it.nuss else it.nuhsa)
                presenter.validateIdentifier("0", editText?.text.toString(), "")
            }

            // Identifier
            if (it.idType.key.isEmpty()) {
                identifier_type_tv?.setItem(identifier_type_tv.adapter.getItem(0) as KeyValueEntity)
            } else {
                if (it.identification.isNotEmpty()) {
                    identifier_type_tv?.setItem(it.idType)
                    identifier_type_tv?.setText(it.idType.value, false)
                }
            }
            if (it.identification.isNotEmpty() && it.identification != MsspaAuthConsts.DEFAULT_DNI_UNDER_AGE) {
                identifier_it.editText?.setText(it.identification)
                presenter.validateIdentifier(
                    identifier_type_tv.currentItem!!.key,
                    identifier_it.editText?.text.toString(), ""
                )
            }
            // Birthday
            birthday_it.editText?.setText(it.birthDate)
            presenter.validateBirthday(birthday_it.editText?.text.toString())
        }
    }

    override fun initUsersButton() {
        usersButton.setOnMenuItemClickListener {
            presenter.onUsersButtonClicked()
            true
        }
    }

    override fun haveBiometricOrPin(): Boolean = BiometricUtils.isBiometricOrSecured(requireContext())

    override fun showSaveUserCheck() {
        save_user_ch?.visibility = View.VISIBLE
    }

    override fun showUsersButton() {
        usersButton.isVisible = true
    }

    override fun hideUsersButton() {
        usersButton.isVisible = false
    }

    override fun hideSaveUserCheck() {
        save_user_ch?.visibility = View.GONE
    }

    override fun hideOverlay() {
        overlay?.visibility = View.GONE
    }

    override fun showOverlay() {
        overlay?.visibility = View.VISIBLE
    }

    override fun showSavedUsers(msspaAuthenticationUsers: List<MsspaAuthenticationUserEntity>) {
        val adapter = UserAdapter(
                onClickItemListener = { userEntity, _ -> presenter.onSelectUser(userEntity) },
                onRemoveItemListener = { userEntity ->
                    presenter.onRemoveUser(
                            userEntity,
                            cryptoManager
                    )
                }
        ).apply {
            setItemsAndNotify(msspaAuthenticationUsers)
        }

        usersDialogMsspaAuth = MsspaAuthCustomBottomSheetDialog.newInstance(adapter = adapter)
        usersDialogMsspaAuth.show(requireFragmentManager(), "")
    }

    override fun hideUsersDialog() {
        usersDialogMsspaAuth.dismiss()
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

    open fun onSendClicked() {
        presenter.onSendClicked(
                saveUser = save_user_ch?.isChecked ?: false,
                nuss = Utils.validateHealthCard(HealthCardEntity.NUSS, health_card_it?.editText?.text.toString()),
                nuhsa = Utils.validateHealthCard(HealthCardEntity.NUHSA, health_card_it?.editText?.text.toString()),
                idType = identifier_type_tv.currentItem!!,
                identification = identifier_it.editText?.text.toString(),
                birthDate = birthday_it.editText?.text.toString()
        )
    }

    override fun disableLoginButton() {
        login_bt.isEnabled = false
    }

    override fun enableLoginButton() {
        login_bt.isEnabled=true
    }

    override fun clearForm() {
        health_card_it.editText?.setText("")
        identifier_type_tv.setItem(0)
        identifier_it.editText?.setText("")
        birthday_it.editText?.setText("")
    }

    override fun showDialogNotPhoneSecured() {
        showWarning(
            message = R.string.dialog_not_phone_secured,
            onAccept = { save_user_ch.isChecked = false }
        )
    }
    //endregion
}