package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic


import es.juntadeandalucia.msspa.saludandalucia.domain.entities.DialogEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EventEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LikeItEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.ExecuteJavascriptUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetServiceEventsUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetSavedLikeItUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SaveLikeItUseCase
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber
import javax.inject.Inject

class DynamicFeedbackController @Inject constructor(
    private val getEventsUseCase: GetServiceEventsUseCase,
    private val saveLikeItUseCase: SaveLikeItUseCase,
    private val getSavedLikeItUseCase: GetSavedLikeItUseCase,
    private val executeJavascriptUseCase: ExecuteJavascriptUseCase
) {

    private lateinit var dynamicActions: DynamicActions

    fun attachListener(dynamicActions: DynamicActions) {
        this.dynamicActions = dynamicActions
    }

    fun loadEvents() {
        getEventsUseCase.execute(onSuccess = { serviceData ->
            getSavedLikeItUseCase.execute(onSuccess = { savedData ->
                if (savedData.lastUpdate != serviceData.lastUpdate) {
                    startUpdating(serviceData, savedData)
                }
            }, onError = {
                saveLikeItUseCase.params(serviceData).execute(onComplete = {}, onError = {})
            })
        }, onError = {
            Timber.e(it)
        })
    }

    private fun startUpdating(serviceData: LikeItEntity, savedData: LikeItEntity) {
        saveLikeItUseCase.params(
            serviceData.copy(
                events = updateEvents(
                    serviceData.events,
                    savedData.events
                )
            )
        ).execute(onComplete = {
            Timber.d("Events updated successfully")
        }, onError = {
            Timber.e(it)
        })
    }

    private fun updateEvents(
        serviceEvents: Map<String, EventEntity>,
        savedEvents: Map<String, EventEntity>
    ): Map<String, EventEntity> {
        val updatedEvents = mutableMapOf<String, EventEntity>()

        serviceEvents.forEach { servEvent ->
            val localEvent = savedEvents[servEvent.key]

            if (servEvent.value.type == Consts.TYPE_RESET) {
                updatedEvents[servEvent.key] = servEvent.value
            } else {
                localEvent?.let { updatedEvents[servEvent.key] = it }
            }
        }

        return updatedEvents
    }

    fun insertEvent(event: String = "") {
        getSavedLikeItUseCase.execute(onSuccess = { likeIt ->
            val oldEvent = likeIt.events[event]
            if (oldEvent != null) {
                likeIt.events[event]!!.counter++

                val eventsMap: MutableMap<String, Int> = mutableMapOf()

                likeIt.events.forEach { (id, eventEntity) ->
                    eventsMap[id] = eventEntity.counter
                }

                saveLikeItUseCase.params(likeIt)
                    .execute(onComplete = {
                        evaluateJavascript(eventsMap, likeIt)
                    }, onError = {
                        Timber.e(it)
                    })
            }
        }, onError = {
            Timber.e(it)
        })
    }

    private fun evaluateJavascript(
        eventsMap: Map<String, Int>,
        likeItEntity: LikeItEntity
    ) {
        likeItEntity.actions.map { action->
            executeJavascriptUseCase.params(eventsMap, action.value.function)
                .buildUseCase(onSuccess = { needLaunch ->
                    if (needLaunch) {
                        action.value.target?.let { target ->
                            evaluationCompleted(likeItEntity.dialog[target]!!, action.key)
                        } ?: dynamicActions.makeDynamicEventsAction(action.key)
                    }
                }, onError = {
                    Timber.e(it)
                })
        }
    }

    private fun evaluationCompleted(dialogEntity: DialogEntity, actionId: String) {
        with(dialogEntity) {
            when (type) {
                Consts.TARGET_DIALOG -> {
                    dynamicActions.showDynamicEventsDialog(
                        title = title,
                        message = message,
                        actionId = actionId
                    )
                }
                else -> {}
            }
        }
    }
}