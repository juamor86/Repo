package es.juntadeandalucia.msspa.saludandalucia.presentation.notifications

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NotificationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.adapter.NotificationsAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.other.SwipeToDeleteCallback
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_notifications.*

/**
 * A simple [Fragment] subclass.
 */
class NotificationsFragment : BaseFragment(), NotificationsContract.View {

    lateinit var itemTouchHelper: ItemTouchHelper
    var removeAllButton: MenuItem? = null

    @Inject
    lateinit var presenter: NotificationsContract.Presenter

    lateinit var adapter: NotificationsAdapter

    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_notifications

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
        presenter.onCreate()
    }

    override fun setupAdapter() {
        this.adapter = NotificationsAdapter(
            presenter::onNotificationItemClick,
            presenter::onNotificationItemRemoved
        )
        itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(context!!, adapter))
    }

    override fun setupNotificationsRecycler() {
        notifications_sw.apply {
            setColorSchemeResources(R.color.colorAccent)
            setOnRefreshListener {
                presenter.refreshNotifications()
            }
        }
        notifications_rv.adapter = adapter
        itemTouchHelper.attachToRecyclerView(notifications_rv)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.remove_notifications, menu)
        removeAllButton = menu.findItem(R.id.remove_notifications_bt)
        super.onCreateOptionsMenu(menu, inflater)
        presenter.onOptionsMenuCreated()
    }

    override fun initRemoveAllButton(visible: Boolean) {
        removeAllButton?.setOnMenuItemClickListener {
            presenter.onRemoveAllButtonClicked()
            true
        }
        removeAllButton?.isVisible = visible
    }

    override fun getNotificationsCount(): Int {
        return adapter.itemCount
    }

    override fun requestConfirmationForRemoveAll() {
        showWarningDialog(
            title = R.string.remove_notifications_dialog_title,
            message = R.string.remove_notifications_dialog_message,
            positiveText = R.string.yes,
            onAccept = {
                presenter.removeAllNotifications()
            },
            cancelText = R.string.no,
            onCancel = {}
        )
    }

    override fun showRemoveAllButton() {
        removeAllButton?.isVisible = true
    }

    override fun hideRemoveAllButton() {
        removeAllButton?.isVisible = false
    }

    override fun openNotificationDetails(notification: NotificationEntity, itemView: View) {
        val bundle = bundleOf(NotificationEntity::class.java.simpleName to notification)

        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            .navigate(
                R.id.action_notifications_to_notification_details, bundle
            )
    }

    override fun showNotifications(notifications: List<NotificationEntity>) {
        empty_group.visibility = View.GONE
        notifications_group.visibility = View.VISIBLE
        notifications_unread_tv.text = countNotificationsUnread(notifications)

        adapter.setItemsAndNotify(notifications)
    }

    private fun countNotificationsUnread(notifications: List<NotificationEntity>): String {
        var notificationsUnread = 0
        for (notification in notifications) {
            if (!notification.readed) {
                notificationsUnread++
            }
        }
        return if (notificationsUnread > 0) {
            getString(R.string.notifications_unread, notificationsUnread.toString())
        } else {
            getString(R.string.notifications_readed)
        }
    }

    override fun hideRefreshing() {
        notifications_sw?.isRefreshing = false
    }

    override fun showEmptyView() {
        empty_group.visibility = View.VISIBLE
        empty_tv.text = getString(R.string.notifications_empty)
        notifications_group.visibility = View.GONE
    }

    override fun showErrorView() {
        //TODO BEFORE NULLABLE IT WAS THERE A CRASH, FIND BETTER SOLUTION
        empty_group?.visibility = View.VISIBLE
        empty_tv?.text = getString(R.string.notifications_error)
        notifications_group?.visibility = View.GONE
    }

    override fun showNotificationsDisabledWarning(notificationsDisable: Boolean) {
        with(notifications_group.visibility == View.VISIBLE) {
            if (this && notificationsDisable) {
                notifications_off_tv.visibility = View.VISIBLE
            } else if (!this && !notificationsDisable) {
                empty_tv.text = getString(R.string.notifications_not_activated)
                empty_group.visibility = View.VISIBLE
            }
        }
    }

    override fun showRemoveNotificationsErrorDialog() {
        showErrorDialog(R.string.error_remove_all_notifications)
    }

   override fun getNotificationTitle()= requireContext().getString(R.string.covid_title_section)

}
