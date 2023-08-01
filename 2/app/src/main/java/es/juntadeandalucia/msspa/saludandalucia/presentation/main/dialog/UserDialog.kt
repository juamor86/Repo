package es.juntadeandalucia.msspa.saludandalucia.presentation.main.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.TopSheetDialog
import kotlinx.android.synthetic.main.view_dialog_user.*
import java.io.File.separator

class UserDialog(
    context: Context,
    private var user: MsspaAuthenticationUserEntity?,
    private val listener: UserDialogListener
) :
    TopSheetDialog(context) {

    override fun bindLayout(): Int = R.layout.view_dialog_user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setListeners()
        showUser()
        checkButton()
    }

    fun setUser(user: MsspaAuthenticationUserEntity?) {
        this.user = user
        showUser()
        checkButton()
    }
    private fun setListeners() {
        login_bt.setOnClickListener {
            dismiss()
            listener.onLoginClicked()
        }
        logout_bt.setOnClickListener {
            dismiss()
            listener.onLogoutClicked()
        }
    }

    private fun showUser() {
        user?.let {
            user_no_iv.visibility = View.GONE
            user_iv.visibility = View.VISIBLE
            user_iv.text = it.initials
            user_name_tv.text = it.prettyName
            user_name_tv.visibility = View.VISIBLE
            user_nuhsa_tv.text = it.nuhsa
            user_nuhsa_tv.visibility = View.GONE
        } ?: run {
            user_no_iv.visibility = View.VISIBLE
            user_iv.visibility = View.GONE
            user_name_tv.visibility = View.GONE
            user_nuhsa_tv.visibility = View.GONE
        }
    }


    private fun checkButton() {
        user?.let {
            login_bt.visibility = View.INVISIBLE
            logout_bt.visibility = View.VISIBLE
        } ?: run {
            login_bt.visibility = View.VISIBLE
            logout_bt.visibility = View.INVISIBLE
        }
    }

    interface UserDialogListener {
        fun onLoginClicked()
        fun onLogoutClicked()
    }
}
