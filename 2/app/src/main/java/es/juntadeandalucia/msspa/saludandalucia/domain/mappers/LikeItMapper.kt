package es.juntadeandalucia.msspa.saludandalucia.domain.mappers


import android.app.Dialog
import es.juntadeandalucia.msspa.saludandalucia.data.entities.LikeItResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.ParameterResponseData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ActionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EventEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LikeItEntity
import es.juntadeandalucia.msspa.saludandalucia.data.entities.feedback.ActionData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.feedback.DialogData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.feedback.EventData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.feedback.LikeItData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.DialogEntity

class LikeItMapper {
    companion object {
        fun convert(serviceData: LikeItResponseData): LikeItEntity =
            with(serviceData) {
                LikeItEntity(
                    lastUpdate = meta.lastUpdated,
                    events = convertEvents(parameter),
                    actions = convertActions(parameter),
                    dialog = convertDialogs(parameter)
                )
            }

        fun convert(likeItData: LikeItData): LikeItEntity =
            with(likeItData) {
                LikeItEntity(
                    lastUpdate = lastUpdate,
                    events = convertMapEventDataToEntity(events),
                    actions = convertMapActionDataToEntity(actions), //actions.map { convert(it) },
                    dialog = convertMapDialogDataToEntity(dialogs)
                )
            }

        private fun convertMapDialogDataToEntity(dialogs: Map<String, DialogData>): Map<String, DialogEntity> {
            val map = mutableMapOf<String, DialogEntity>()
            dialogs.map { (id, dialogData) ->
                with(dialogData) {
                    map.put(id, DialogEntity(title = title, message = message, type = type))
                }
            }
            return map
        }

        private fun convertMapActionDataToEntity(actions: Map<String, ActionData>): Map<String, ActionEntity> {
            val map = mutableMapOf<String, ActionEntity>()
            actions.map { (id, actionData) ->
                with(actionData) {
                    map.put(id, ActionEntity(function = function, target = target))
                }
            }
            return map
        }

        private fun convertMapEventDataToEntity(events: Map<String, EventData>): Map<String, EventEntity> {
            val map = mutableMapOf<String, EventEntity>()
            events.map { (id, eventData) ->
                with(eventData) {
                    map.put(id, EventEntity(counter = counter, accessLevel = accessLevel))
                }
            }
            return map
        }

        fun convert(likeItEntity: LikeItEntity): LikeItData =
            with(likeItEntity) {
                LikeItData(
                    lastUpdate = lastUpdate,
                    events = convertMapEventEntityToData(events),
                    actions = convertMapActionsEntityToData(actions),
                    dialogs = convertMapDialogsEntityToData(dialog)
                )
            }

        private fun convertMapEventEntityToData(events: Map<String, EventEntity>): Map<String, EventData> {
            val map = mutableMapOf<String, EventData>()
            events.map { (id, eventEntity) ->
                with(eventEntity) {
                    map.put(id, EventData(counter = counter, accessLevel = accessLevel))
                }
            }
            return map
        }

        private fun convertMapActionsEntityToData(events: Map<String, ActionEntity>): Map<String, ActionData> {
            val map = mutableMapOf<String, ActionData>()
            events.map { (id, eventEntity) ->
                with(eventEntity) {
                    map.put(id, ActionData(function = function, target = target))
                }
            }
            return map
        }

        private fun convertMapDialogsEntityToData(events: Map<String, DialogEntity>): Map<String, DialogData> {
            val map = mutableMapOf<String, DialogData>()
            events.map { (id, eventEntity) ->
                with(eventEntity) {
                    map.put(id, DialogData(title = title, message = message, type = type))
                }
            }
            return map
        }


        //TODO refactor this methods below
        private fun convertActions(parameter: List<ParameterResponseData>): Map<String, ActionEntity> {
            val map = mutableMapOf<String, ActionEntity>()
            val actionChildren = parameter.filter { it.id == "actions" }
            actionChildren.first().children.map { actionData ->
                with(actionData) {
                    map.put(id, ActionEntity(function = access_level, target = target))
                }
            }
            return map
        }

        private fun convertEvents(parameter: List<ParameterResponseData>): Map<String, EventEntity> {
            val map = mutableMapOf<String, EventEntity>()
            val eventsChildren = parameter.filter { it.id == "events" }
            eventsChildren.first().children.map { eventData ->
                with(eventData) {
                    map.put(
                        id, EventEntity(
                            type = type,
                            accessLevel = access_level
                        )
                    )
                }
            }
            return map
        }

        private fun convertDialogs(parameter: List<ParameterResponseData>): Map<String, DialogEntity> {
            val map = mutableMapOf<String, DialogEntity>()
            val eventsChildren = parameter.filter { it.id == "targets" }
            eventsChildren.first().children.map { eventData ->
                with(eventData) {
                    map.put(
                        id, DialogEntity(title = title!!.text, message = title.alt, type = type!!)
                    )
                }
            }
            return map
        }
    }
}

