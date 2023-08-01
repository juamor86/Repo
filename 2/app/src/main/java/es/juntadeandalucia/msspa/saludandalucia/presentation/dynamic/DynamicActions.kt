package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.DialogEntity

interface DynamicActions {

    fun showActionsDialog(dialogEntity: DialogEntity)
    fun showDynamicEventsDialog(title: String, message: String, actionId:String)
    fun makeDynamicEventsAction(action:String)
}