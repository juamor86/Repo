package es.juntadeandalucia.msspa.authentication.domain.mappers

import es.juntadeandalucia.msspa.authentication.data.factory.entities.KeyValueData
import es.juntadeandalucia.msspa.authentication.data.factory.entities.UserData
import es.juntadeandalucia.msspa.authentication.domain.entities.KeyValueEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity

class UserMapper {
    companion object {

        fun convert(users: List<UserData>) = users.map { convert(it) }

        fun convert(model: UserData) = MsspaAuthenticationUserEntity(
            name = model.name,
            nuss = model.nuss,
            nuhsa = model.nuhsa,
            birthDate = model.birthDate,
            idType = model.idType?.run { KeyValueEntity(key, value) } ?: KeyValueEntity("", ""),
            identification = model.identification,
            phone = model.phone
        )

        fun convert(model: MsspaAuthenticationUserEntity) = UserData(
            name = model.name,
            nuss = model.nuss,
            nuhsa = model.nuhsa,
            birthDate = model.birthDate,
            idType = KeyValueData(model.idType.key, model.idType.value),
            identification = model.identification,
            phone = model.phone
        )
    }
}