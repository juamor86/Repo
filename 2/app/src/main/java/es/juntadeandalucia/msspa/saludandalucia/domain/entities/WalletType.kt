package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import androidx.annotation.StringRes
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

enum class WalletType( @StringRes val title: Int, val type: String) {
    CONTRAINDICATION( R.string.andalussian_contraindications, Consts.TYPE_CONTRAINDICATIONS),
    VACCINE (R.string.greenpass_vaccinate, Consts.TYPE_VACCINATION),
    RECOVERY(R.string.greenpass_recovery, Consts.TYPE_RECOVERY),
    TEST(R.string.greenpass_test, Consts.TYPE_TEST)
}