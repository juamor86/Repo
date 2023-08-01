package es.juntadeandalucia.msspa.saludandalucia.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "certificates")
data class WalletData(
    @PrimaryKey
    var id: String,
    var type: Int,
    var jwt: String
)

