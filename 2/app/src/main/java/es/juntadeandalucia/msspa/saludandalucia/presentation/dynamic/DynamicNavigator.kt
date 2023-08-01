package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic

import android.os.Bundle
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity

interface DynamicNavigator {

    fun handleNavigation(dest: NavigationEntity)
    fun navigateToSection(dest: Int, bundle: Bundle? = null)
    fun navigateToDynamicScreen(screen: DynamicScreenEntity)
    fun navigateToWebview(url: String)
    fun navigateToClicSaludWebview(dest: NavigationEntity)
    fun navigateToExternal(url: String)
    fun higherAccessRequired(dest: NavigationEntity)
    fun informSectionNotAvailable()
    fun informSessionExpired()
    fun informErrorAndHome()
    fun informNotificationsNotEnabled(dest: NavigationEntity)
}
