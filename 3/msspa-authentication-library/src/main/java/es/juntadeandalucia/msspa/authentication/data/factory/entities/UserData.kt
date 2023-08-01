package es.juntadeandalucia.msspa.authentication.data.factory.entities

data class UserData(
        val name: String = "",
        val nuss: String = "",
        val nuhsa: String = "",
        var idType: KeyValueData? = KeyValueData("", ""),
        val identification: String = "",
        val birthDate: String = "",
        val phone: String = ""
) {

    override fun equals(user: Any?) =
            user is UserData && (
                    (user.identification.isNotEmpty() && identification.isNotEmpty() && user.identification == identification) ||
                            (user.nuhsa.isNotEmpty() && nuhsa.isNotEmpty() && user.nuhsa == nuhsa) ||
                            (user.name?.isNotEmpty() ?: false && name?.isNotEmpty() ?: false && user.name == name))

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + nuhsa.hashCode()
        result = 31 * result + (idType?.hashCode() ?: 0)
        result = 31 * result + identification.hashCode()
        result = 31 * result + birthDate.hashCode()
        result = 31 * result + phone.hashCode()
        return result
    }
}
