package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class QuizUserData(
    val name: String = "",
    val nuhsa: String = "",
    var idType: KeyValueData? = KeyValueData("", ""),
    val identification: String = "",
    val birthDate: String = "",
    val phone: String = "",
    val prefixPhone: String = "",
    var isHealthProf: Boolean = false,
    var isSecurityProf: Boolean = false,
    var isSpecialProf: Boolean = false

) {

    override fun equals(user: Any?) =
        user is QuizUserData && (
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
        result = 31 * result + prefixPhone.hashCode()
        result = 31 * result + (isHealthProf?.hashCode() ?: 0)
        result = 31 * result + (isSecurityProf?.hashCode() ?: 0)
        result = 31 * result + (isSpecialProf?.hashCode() ?: 0)
        return result
    }
}
