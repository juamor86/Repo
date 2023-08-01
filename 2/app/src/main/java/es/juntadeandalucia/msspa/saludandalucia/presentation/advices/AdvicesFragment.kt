package es.juntadeandalucia.msspa.saludandalucia.presentation.advices

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypes
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.adapter.AdvicesAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.dialog.AdviceDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.onboarding.AvisasHubOnboardingDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.MainActivity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_advices_list.advice_new_advice_btn
import kotlinx.android.synthetic.main.fragment_advices_list.all_tab_btn
import kotlinx.android.synthetic.main.fragment_advices_list.created_advices_rv
import kotlinx.android.synthetic.main.fragment_advices_list.created_tab_btn
import kotlinx.android.synthetic.main.fragment_advices_list.created_tv
import kotlinx.android.synthetic.main.fragment_advices_list.first_advice_cl
import kotlinx.android.synthetic.main.fragment_advices_list.list_fm
import kotlinx.android.synthetic.main.fragment_advices_list.new_advice_btn
import kotlinx.android.synthetic.main.fragment_advices_list.received_advices_rv
import kotlinx.android.synthetic.main.fragment_advices_list.received_tab_btn
import kotlinx.android.synthetic.main.fragment_advices_list.received_tv
import kotlinx.android.synthetic.main.fragment_advices_list.swipe_advice_list


class AdvicesFragment : BaseFragment(), AdvicesContract.View {

    //region VARIABLES
    @Inject
    lateinit var presenter: AdvicesContract.Presenter

    lateinit var createdAdapter: AdvicesAdapter
    lateinit var receivedAdapter: AdvicesAdapter

    var shared = listOf<AdviceEntity>()
    var sharedWithMe = listOf<AdviceEntity>()
    var adviceDialog: AdviceDialog? = null
    //endregion

    //region INITIALIZATION & OVERRIDES
    override fun bindPresenter() = presenter

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
        (requireActivity() as MainActivity).getPendingNavigationDestination().let {
            presenter.onCreate((requireActivity() as MainActivity).pendingNavDestination)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.removeAdviceCatalogTypePreferences()
    }

    override fun bindLayout() = R.layout.fragment_advices_list

    override fun setupTypeTabButtons() {
        all_tab_btn.setOnClickListener {
            presenter.onTabButtonPressed(AdviceTypes.ALL)
        }

        created_tab_btn.setOnClickListener {
            presenter.onTabButtonPressed(AdviceTypes.CREATED)
        }

        received_tab_btn.setOnClickListener {
            presenter.onTabButtonPressed(AdviceTypes.RECEIVED)
        }
    }

    override fun setupScreenStatus(showEmptyScreen: Boolean) {
        if (showEmptyScreen) {
            first_advice_cl?.visibility = VISIBLE
            list_fm?.visibility = GONE
            new_advice_btn?.visibility = GONE

            advice_new_advice_btn?.setOnClickListener {
                presenter.onNewAdvicePressed()
            }
        } else {
            first_advice_cl?.visibility = GONE
            list_fm?.visibility = VISIBLE
            new_advice_btn?.visibility = VISIBLE

            new_advice_btn?.setOnClickListener {
                presenter.onNewAdvicePressed()
            }
        }

        swipe_advice_list?.setOnRefreshListener {
            presenter.loadAdvices()
        }

        swipe_advice_list?.isRefreshing = false
    }

    override fun changeStatusTypeTab(type: AdviceTypes) {
        when (type) {
            AdviceTypes.ALL -> {
                changeToPressedTabButton(all_tab_btn)
                changeToUnPressedTabButton(created_tab_btn)
                changeToUnPressedTabButton(received_tab_btn)
            }
            AdviceTypes.CREATED -> {
                changeToUnPressedTabButton(all_tab_btn)
                changeToPressedTabButton(created_tab_btn)
                changeToUnPressedTabButton(received_tab_btn)
            }
            AdviceTypes.RECEIVED -> {
                changeToUnPressedTabButton(all_tab_btn)
                changeToUnPressedTabButton(created_tab_btn)
                changeToPressedTabButton(received_tab_btn)
            }
        }
    }

    override fun setupAdapter() {
        this.createdAdapter = AdvicesAdapter(
            presenter::onNotificationItemClick
        )

        this.receivedAdapter = AdvicesAdapter(
            presenter::onNotificationItemClick
        )

        created_advices_rv.adapter = createdAdapter
        received_advices_rv.adapter = receivedAdapter
    }

    override fun refillAdvicesList(
        advices: List<AdviceEntity>,
        advicesReceived: List<AdviceEntity>,
        advicesType: AdviceTypes
    ) {

        created_tv.visibility = GONE
        received_tv.visibility = GONE

        shared = advices
        sharedWithMe = advicesReceived

        when (advicesType) {
            AdviceTypes.ALL -> {
                showCreatedAdvices()
                showReceivedAdvices()
            }
            AdviceTypes.CREATED -> {
                received_advices_rv.visibility = GONE
                showCreatedAdvices()
            }
            AdviceTypes.RECEIVED -> {
                created_advices_rv.visibility = GONE
                showReceivedAdvices()
            }
        }
    }

    private fun showReceivedAdvices() {
        if(sharedWithMe.isNotEmpty()) {
            receivedAdapter.setItemsAndNotify(sharedWithMe)
            received_advices_rv.visibility = VISIBLE
            received_tv.visibility = VISIBLE
        }else{
            received_advices_rv.visibility = GONE
            received_tv.visibility = GONE
            receivedAdapter.setItemsAndNotify(listOf())
        }
    }

    private fun showCreatedAdvices() {
        if(shared.isNotEmpty()) {
            createdAdapter.setItemsAndNotify(shared)
            created_advices_rv.visibility = VISIBLE
            created_tv.visibility = VISIBLE
        }else{
            created_advices_rv.visibility = GONE
            created_tv.visibility = GONE
            createdAdapter.setItemsAndNotify(listOf())
        }
    }

    override fun showConfirmDialog() {}

    override fun navigateToDetail(
        nuhsa: String,
        advice: AdviceEntity,
        phoneNumber: String
    ) {
        val bundle = bundleOf(
            Consts.ARG_ADVICES_SHARED to advice,
            Consts.ARG_ADVICE_NUHSA to nuhsa.trim(),
            Consts.ARG_ADVICE_PHONE to phoneNumber
        )
        findNavController().navigate(R.id.action_advices_to_detail_advice, bundle)
    }

    override fun navigateToType(nuhsa: String, phoneNumber: String, advices: List<AdviceEntity>) {
        val bundle = bundleOf(
            Consts.ARG_ADVICE_NUHSA to nuhsa.trim(),
            Consts.ARG_ADVICES_SHARED to advices,
            Consts.ARG_ADVICE_PHONE to phoneNumber
        )
        findNavController().navigate(R.id.action_advices_to_advice_type, bundle)
    }
    //endregion

    //region METHODS
    private fun changeToPressedTabButton(button: AppCompatButton) {
        button.setBackgroundResource(R.drawable.sh_item_tab_selector_button_pressed)
        button.elevation = 5.0F

        activity?.let {
            button.setTextColor(ContextCompat.getColor(it, R.color.colorAccent))
        }
    }

    private fun changeToUnPressedTabButton(button: AppCompatButton) {
        button.setBackgroundResource(R.drawable.sh_item_tab_selector_button_unpressed)
        button.elevation = 0.0F

        activity?.let {
            button.setTextColor(ContextCompat.getColor(it, R.color.black))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.advice_on_boarding_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.on_boarding_bt) {
            showAdviceDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showAdviceDialog() {
        val onBoarding = AvisasHubOnboardingDialog(onClose = {
            presenter.saveFirstOpenToAdvice()
        })
        activity?.supportFragmentManager?.apply {
            onBoarding.show(this, Consts.ON_BOARDING_DIALOG_TAG)
        }
    }

    //TODO reset contacts
    /*override fun checkContactsPermissions() {
        presenter.apply {
            when {
                PermissionsUtil.checkIsContactPermissionGranted(requireContext()) ->
                    loadAdvices()
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) ->
                    permissionNotGranted()
                else ->
                    permissionNotGranted()
            }
        }
    }*/

   /* override fun requestPermission() {
        permissionsLauncher.launch(Manifest.permission.READ_CONTACTS)
    }*/

    /*private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                presenter.loadAdvices()
            }
        }*/
    //endregion
}