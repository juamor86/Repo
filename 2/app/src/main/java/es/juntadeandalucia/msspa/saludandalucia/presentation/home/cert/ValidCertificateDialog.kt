package es.juntadeandalucia.msspa.saludandalucia.presentation.home.cert

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UECovidCertEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.BindableCustomFullScreenDialog
import kotlinx.android.synthetic.main.view_certificate_valid.*

class ValidCertificateDialog(
    private val userCovidCertEntity: UECovidCertEntity,
    override val onDismiss: (() -> Any)? = null
) :
    BindableCustomFullScreenDialog() {

    override fun bindContentLayout() = R.layout.view_certificate_valid

    override fun bindData() {
       if (userCovidCertEntity.dni != null) {
           contraindication_cert_gp.visibility = View.VISIBLE
           eu_cert_gp.visibility = View.GONE
           dni_value_tv.text = userCovidCertEntity.dni
           title_tv.text = resources.getText(R.string.validation_contraindication_cert_title)
       } else {
           eu_cert_gp.visibility = View.VISIBLE
           contraindication_cert_gp.visibility = View.GONE
           title_tv.text = resources.getText(R.string.validation_ccd_cert_title)
           name_value_tv.text = userCovidCertEntity.userName
           lastname_value_tv.text = userCovidCertEntity.userLastName
           date_value_tv.text = userCovidCertEntity.birthdate
           fullname_tv.text = userCovidCertEntity.fullName
       }
    }
}
