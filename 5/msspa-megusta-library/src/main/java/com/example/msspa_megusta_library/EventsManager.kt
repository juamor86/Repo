package com.example.msspa_megusta_library

import com.example.msspa_megusta_library.data.entities.ServiceData
import com.example.msspa_megusta_library.domain.entity.ActionEntity
import com.example.msspa_megusta_library.domain.entity.EventEntity
import com.example.msspa_megusta_library.domain.entity.LikeItEntity
import com.example.msspa_megusta_library.domain.usecases.GetEventsUseCase
import com.example.msspa_megusta_library.domain.usecases.GetSavedLikeItUseCase
import com.example.msspa_megusta_library.domain.usecases.SaveLikeItUseCase
import javax.inject.Inject

class EventsManager @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val saveLikeItUseCase: SaveLikeItUseCase,
    private val getSavedLikeItUseCase: GetSavedLikeItUseCase
) {

    fun loadEvents() {
        getEventsUseCase.execute(onSuccess = { serviceData ->
            getSavedLikeItUseCase.execute(onSuccess = { savedData ->
                if (savedData.lastUpdate != serviceData.lastUpdate) {
                    startUpdating(serviceData, savedData)
                }
            }, onError = {
                saveLikeItUseCase.params(serviceData).execute(onComplete = {

                }, onError = {})
            })
        }, onError = {})
    }

    private fun startUpdating(serviceData: LikeItEntity, savedData: LikeItEntity) {
        val newLikeData = LikeItEntity(
            lastUpdate = serviceData.lastUpdate,
            events = updateEvents(serviceData, savedData),
            actions = serviceData.actions
        )
        saveLikeItUseCase.params(newLikeData).execute(onComplete = {}, onError = {})
    }

    //TODO end this function
    private fun updateEvents(
        serviceData: LikeItEntity,
        savedData: LikeItEntity
    ): List<EventEntity> = listOf()

    fun insertEvent(event: String) {

    }


}