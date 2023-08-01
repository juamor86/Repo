package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonObject
import es.juntadeandalucia.msspa.saludandalucia.data.entities.UserCovidCertData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UserCovidCertEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat

class UserCovidCertMapper {

    companion object {

        fun convert(model: UserCovidCertData) = with(model) {
            if (content.isNotEmpty()) {
                val data = content[0].attachment.data
                val jwt = String(Base64.decode(data, Base64.DEFAULT))
                val json = String(Base64.decode(jwt.split(".")[1], Base64.DEFAULT))
                val jsonObj = Gson().fromJson(json, JsonObject::class.java)
                val vaccineDate: String? = jsonObj.get("fechaVacunacion").asString
                val userName: String = jsonObj.get("nombre")?.asString ?: ""
                val userLastName: String =
                    (jsonObj.get("primerApellido")?.asString ?: "").plus(" ").plus(
                        jsonObj.get("segundoApellido")?.asString ?: ""
                    )
                val userId: String = jsonObj.get("id")?.asString ?: ""
                vaccineDate?.let {
                    val vaccineDateFormatted =
                        UtilDateFormat.stringToDate(it, UtilDateFormat.DATE_FORMAT_TZ)
                            ?.let { it1 -> UtilDateFormat.dateToString(it1, "dd/MM/yyyy") }
                    return UserCovidCertEntity(
                        userName = userName,
                        userLastName = userLastName,
                        userId = userId,
                        birthdate = vaccineDateFormatted,
                        jwt = jwt,
                    )
                }
            }
            UserCovidCertEntity()
        }
    }
}
