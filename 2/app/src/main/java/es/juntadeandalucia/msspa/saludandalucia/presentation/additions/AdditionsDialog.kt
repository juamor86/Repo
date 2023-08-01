package es.juntadeandalucia.msspa.saludandalucia.presentation.additions

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicReleasesEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicReleasesSectionEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomFullScreenDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicConsts
import kotlinx.android.synthetic.main.view_additions_dialog.*
import kotlinx.android.synthetic.main.view_item_permissions.view.*

class AdditionsDialog(
    private val onAccept: (Boolean) -> Unit,
    val dynamicReleasesSectionEntity: DynamicReleasesSectionEntity
) : CustomFullScreenDialog() {

    override fun bindContentLayout(): Int = R.layout.view_additions_dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        setStyle(STYLE_NORMAL, R.style.BehaviorDraggable)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageView>(R.id.close_iv).visibility = View.GONE
        view.findViewById<ImageView>(R.id.image_header_iv).visibility = View.GONE
        initializeEvents()
        buildDialogScreen()
    }

    private fun buildDialogScreen() {
        dynamicReleasesSectionEntity.releases.forEach { parameter ->
            if (parameter.id == DynamicConsts.Releases.DYNAMIC_RELEASES_ID) {
                buildContentReleases(parameter)
            } else if (parameter.id == DynamicConsts.Releases.DYNAMIC_RELEASES_CHECK_ID) {
                buildCheck(parameter)
            }
        }
    }

    private fun buildContentReleases(parameter: DynamicReleasesEntity) {
        //loadIconDynamicReleases(parameter)
        releases_title_tv.text = parameter.title
        releases_description_tv.text = parameter.titleAlt
        parameter.children?.let {
            for (child in it) {
                val childView = layoutInflater.inflate(
                    R.layout.view_item_permissions,
                    releases_content_ll,
                    false
                )
                childView.permission_name_tv.text = child.title.text
                childView.permission_subtitle_tv.text = child.title.alt
                releases_content_ll.addView(childView)
            }
        }
    }

    private fun buildCheck(parameter: DynamicReleasesEntity) {
        releases_agreement_cb.visibility = View.VISIBLE
        releases_agreement_cb.setText(parameter.title)
    }

    private fun initializeEvents() {
        releseases_accept_btn.setOnClickListener {
            dialog?.dismiss()
            onAccept.invoke(releases_agreement_cb.isChecked)
        }
    }

    private fun loadIconDynamicReleases(parameter: DynamicReleasesEntity) {
        parameter.header?.let {
            Picasso.get().load(it).into(releases_icon_iv)
        } ?: enableReleasesIcon(View.GONE)
    }

    private fun enableReleasesIcon(visibility: Int) {
        releases_icon_iv.visibility = visibility
    }

}