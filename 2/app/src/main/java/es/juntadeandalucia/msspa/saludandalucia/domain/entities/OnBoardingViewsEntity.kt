package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import es.juntadeandalucia.msspa.saludandalucia.R

enum class OnBoardingViewsEntity(
    @StringRes val title: Int,
    @StringRes val text: Int,
    @DrawableRes val image: Int
) {
    DESCRIPTION(
        R.string.hub_on_boarding_title,
        R.string.hub_on_boarding_description,
        R.drawable.ic_certificate
    ),
    EUROPEAN_CERTIFICATE(
        R.string.hub_on_boarding_european_title,
        R.string.hub_on_boarding_european_description,
        R.drawable.ic_europe_vaccinate
    ),
    RECOVERY_CERTIFICATE(
        R.string.hub_on_boarding_recovery_title,
        R.string.hub_on_boarding_recovery_description,
        R.drawable.ic_recovery
    ),
    PCR_CERTIFICATE(
        R.string.hub_on_boarding_pcr_title,
        R.string.hub_on_boarding_pcr_description,
        R.drawable.ic_pcr
    ),
    CONTRAINDICATION_CERTIFICATE(
        R.string.hub_on_boarding_contraindication_title,
        R.string.hub_on_boarding_contraindication_description,
        R.drawable.ic_contraindication_onboarding
    )
}
