package es.inteco.conanmobile.data.entities

/**
 * Register device request data
 *
 * @property idTerminal
 * @constructor Create empty Register device request data
 */
class RegisterDeviceRequestData (
    val idTerminal: String
){

    override fun equals(other: Any?): Boolean {
        return other is RegisterDeviceRequestData && other.idTerminal == this.idTerminal
    }
}