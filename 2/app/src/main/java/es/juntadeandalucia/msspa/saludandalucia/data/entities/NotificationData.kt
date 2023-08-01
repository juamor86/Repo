package es.juntadeandalucia.msspa.saludandalucia.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationData(
    @PrimaryKey
    var id: String,
    var title: String,
    var description: String,
    var date: Long,
    var readed: Boolean
)
