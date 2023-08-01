package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import es.juntadeandalucia.msspa.saludandalucia.R

enum class OnBoardingAvisasViewEntity(
    @StringRes val title: Int,
    @StringRes val text: Int,
    @DrawableRes val image: Int
) {
    DESCRIPTION(
        R.string.avisas_onboarding_first_page_title,
        R.string.avisas_onboarding_first_page_body,
        R.drawable.ic_illustrationsavisas
    )/*,
    EUROPEAN_CERTIFICATE(
        R.string.avisas_onboarding_second_page_title,
        R.string.avisas_onboarding_second_page_body,
        R.drawable.ic_illustrationsavisas
    )*/
}