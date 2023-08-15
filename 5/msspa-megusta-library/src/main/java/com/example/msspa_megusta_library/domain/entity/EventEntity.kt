package com.example.msspa_megusta_library.domain.entity

data class EventEntity(
    val id: String,
    val counter: Int = 0,
    val type: String? = null,
    val accessLevel: String? = null
)
