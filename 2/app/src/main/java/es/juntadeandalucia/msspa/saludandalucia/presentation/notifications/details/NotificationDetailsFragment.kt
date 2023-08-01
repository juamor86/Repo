package es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NotificationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_notification_details.*

/**
 * A simple [Fragment] subclass.
 */
class NotificationDetailsFragment : BaseFragment(), NotificationDetailsContract.View {

    lateinit var removeButton: MenuItem

    @Inject
    lateinit var presenter: NotificationDetailsContract.Presenter

    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_notification_details

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val notificationEntity =
            arguments?.get(NotificationEntity::class.java.simpleName) as NotificationEntity
        presenter.setupView(notificationEntity)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.remove_notifications, menu)
        removeButton = menu.findItem(R.id.remove_notifications_bt)
        super.onCreateOptionsMenu(menu, inflater)
        presenter.onOptionsMenuCreated()
    }

    override fun initRemoveButton() {
        removeButton.setOnMenuItemClickListener {
            presenter.onRemoveButtonClicked()
            true
        }
    }

    override fun back() {
        findNavController().popBackStack()
    }

    override fun showNotification(notificationEntity: NotificationEntity) {
        with(notificationEntity) {
            notification_details_title_tv.text = title
            notification_details_date_tv.text = UtilDateFormat.getDateFormatter(date)
            notification_details_hour_tv.text = UtilDateFormat.getHoursDate(date)
            notification_details_description_tv.text =
                description?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
        }
    }

    override fun showRemoveNotificationConfirmDialog() {
        showConfirmDialog(
            R.string.remove_notifications_dialog_title,
            R.string.remove_notification_dialog_message,
            onAccept = { presenter.removeNotification() },
            onCancel = {})
    }
}
