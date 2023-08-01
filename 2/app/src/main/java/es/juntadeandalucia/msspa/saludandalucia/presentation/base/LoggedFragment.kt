package es.juntadeandalucia.msspa.saludandalucia.presentation.base

import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.MainActivity

abstract class LoggedFragment : BaseFragment(), LoggedContract.View {

    override fun informUserNotLogged() {
        if (isAdded) {
            showConfirmDialog(
                title = R.string.user_not_logged,
                onAccept = { activity?.let { (requireActivity() as MainActivity).launchLogin() } },
                onCancel = { findNavController().navigateUp() })
        }
    }

    override fun informUserNotLoggedHighLevel() {
        if (isAdded) {
            showConfirmDialog(
                title = R.string.user_not_logged,
                onAccept = { activity?.let { (requireActivity() as MainActivity).launchLogin(requiredScope = "conf/V") } },
                onCancel = { findNavController().navigateUp() })
        }
    }

    override fun informUserNeedsHigherLevel() {
        showConfirmDialog(
            title = R.string.user_logged_with_low_permissions,
            onAccept = { activity?.let { (requireActivity() as MainActivity).launchLogin(requiredScope = "conf/V") } },
            onCancel = { findNavController().navigateUp() }
        )
    }
}
