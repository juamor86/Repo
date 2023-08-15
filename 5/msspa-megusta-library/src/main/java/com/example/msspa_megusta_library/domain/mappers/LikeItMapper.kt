package com.example.msspa_megusta_library.domain.mappers

import com.example.msspa_megusta_library.data.entities.*
import com.example.msspa_megusta_library.domain.entity.ActionEntity
import com.example.msspa_megusta_library.domain.entity.EventEntity
import com.example.msspa_megusta_library.domain.entity.LikeItEntity

class LikeItMapper {
    companion object {
        fun convert(serviceData: ServiceData): LikeItEntity =
            with(serviceData) {
                LikeItEntity(
                    lastUpdate = meta.lastUpdated,
                    events = convertEvents(parameter),
                    actions = convertActions(parameter)
                )
            }

        fun convert(likeItData: LikeItData): LikeItEntity =
            with(likeItData) {
                LikeItEntity(
                    lastUpdate = lastUpdate,
                    events = events.map { convert(it) },
                    actions = actions.map { convert(it) }
                )
            }

        fun convert(likeItEntity: LikeItEntity): LikeItData =
            with(likeItEntity) {
                LikeItData(
                    lastUpdate = lastUpdate,
                    events = events.map { convert(it) },
                    actions = actions.map { convert(it) }
                )
            }

        private fun convert(eventData: EventData): EventEntity =
            with(eventData) {
                EventEntity(
                    id = id, counter = counter, accessLevel = accessLevel
                )
            }


        private fun convert(actionData: ActionData): ActionEntity =
            with(actionData) {
                ActionEntity(
                    id = id, function = function
                )
            }

        private fun convert(eventEntity: EventEntity): EventData =
            with(eventEntity) {
                EventData(
                    id = id, counter = counter, accessLevel = accessLevel!!
                )
            }


        private fun convert(actionEntity: ActionEntity): ActionData =
            with(actionEntity) {
                ActionData(
                    id = id, function = function
                )
            }

        private fun convertActions(parameter: List<Parameter>): List<ActionEntity> {
            val actionChildren = parameter.filter { it.id == "actions" }
            return actionChildren.first().children.map { actionData ->
                with(actionData) {
                    ActionEntity(id, function = access_level)
                }
            }
        }

        private fun convertEvents(parameter: List<Parameter>): List<EventEntity> {
            val eventsChildren = parameter.filter { it.id == "events" }
            return eventsChildren.first().children.map { eventData ->
                with(eventData) {
                    EventEntity(
                        id = id,
                        type = type,
                        accessLevel = access_level
                    )
                }
            }
        }
    }
}

