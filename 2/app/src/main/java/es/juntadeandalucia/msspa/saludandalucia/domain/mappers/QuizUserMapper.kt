package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.KeyValueData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizUserData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.KeyValueEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizUserEntity

class QuizUserMapper {

    companion object {

        fun convert(users: List<QuizUserData>) = users.map { convert(it) }

        fun convert(model: QuizUserData) = QuizUserEntity(
            name = model.name ?: "",
            nuhsa = model.nuhsa,
            birthDate = model.birthDate,
            idType = model.idType?.run { KeyValueEntity(key, value) } ?: KeyValueEntity("", ""),
            identification = model.identification,
            phone = model.phone,
            prefixPhone = model.prefixPhone,
            isHealthProf = model.isHealthProf ?: false,
            isSpecialProf = model.isSpecialProf ?: false,
            isSecurityProf = model.isSecurityProf ?: false
        )

        fun convert(model: QuizUserEntity) = QuizUserData(
            name = model.name,
            nuhsa = model.nuhsa,
            birthDate = model.birthDate,
            idType = KeyValueData(model.idType.key, model.idType.value),
            identification = model.identification,
            phone = model.phone,
            prefixPhone = model.prefixPhone,
            isHealthProf = model.isHealthProf,
            isSpecialProf = model.isSpecialProf,
            isSecurityProf = model.isSecurityProf
        )
    }
}
