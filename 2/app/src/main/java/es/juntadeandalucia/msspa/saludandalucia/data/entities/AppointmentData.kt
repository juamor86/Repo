package es.juntadeandalucia.msspa.saludandalucia.data.entities

import com.google.gson.annotations.SerializedName

data class AppointmentData(
    @SerializedName("appointment_code")
    val id: String,
    val date: String,
    val hour: String,
    @SerializedName("center_name")
    val center: String,
    @SerializedName("location_name")
    val location: String,
    @SerializedName("diary_name")
    val topic: String,
    @SerializedName("task_name")
    val task: String,
    @SerializedName("activity_name")
    val activity: String
)
