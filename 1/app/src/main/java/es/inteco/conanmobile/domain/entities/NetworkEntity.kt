package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Network entity
 *
 * @property macAddress
 * @property name
 * @property isSafe
 * @constructor Create empty Network entity
 */
@Parcelize
data class NetworkEntity(
    var macAddress:String,                                  //Dirección Mac de la Red, User Story CO2-33.
    var name :String,                                        //Nombre de la Red, User Story CO2-33.
    var isSafe :Int                                         //Es una red segura, User Story CO2-33.
) : Parcelable {
    constructor() : this("","",-1)
}