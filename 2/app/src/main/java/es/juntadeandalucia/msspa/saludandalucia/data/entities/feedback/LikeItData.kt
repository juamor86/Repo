package es.juntadeandalucia.msspa.saludandalucia.data.entities.feedback


data class LikeItData(
    val lastUpdate: String,
    val events: Map<String, EventData>,
    val actions: Map<String, ActionData>,
    val dialogs: Map<String, DialogData>
)