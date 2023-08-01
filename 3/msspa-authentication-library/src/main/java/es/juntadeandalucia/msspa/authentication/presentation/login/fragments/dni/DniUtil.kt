package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.dni

import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.jce.X509Principal
import java.security.cert.X509Certificate

class DniUtil {

    companion object {
        fun getCN(certificate: X509Certificate): String {
            val name = X509Principal(certificate.subjectDN.toString())
            return name.getValues(BCStyle.CN)[0].toString()
        }

        fun getNIF(certificate: X509Certificate): String {
            val name = X509Principal(certificate.subjectDN.toString())
            return name.getValues(BCStyle.SN)[0].toString()
        }
    }

}