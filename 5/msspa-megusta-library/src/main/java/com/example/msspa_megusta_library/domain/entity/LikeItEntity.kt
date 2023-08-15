package com.example.msspa_megusta_library.domain.entity


data class LikeItEntity(val lastUpdate: String, val events: List<EventEntity>, val actions: List<ActionEntity>)
